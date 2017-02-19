package io.anuke.koru.server;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.World;
import io.anuke.koru.server.world.TerrainGenerator;
import io.anuke.koru.server.world.WorldFile;
import io.anuke.koru.systems.*;
import io.anuke.koru.world.Chunk;
import io.anuke.ucore.util.ColorCodes;

public class KoruUpdater{
	KoruServer server;
	public KoruEngine engine;
	public World world;
	public final WorldFile file;
	private boolean isRunning = true;
	private CopyOnWriteArrayList<SendRequest> sendQueue = new CopyOnWriteArrayList<SendRequest>(); //TODO remove this
	final int maxfps = 60;
	long frameid;
	float delta = 1f;
	long lastFpsTime;
	final int blockupdatetime = 60 * 6;
	CountDownLatch latch = new CountDownLatch(1);
	int threads = 0;
	int totalchunks = 0;
	boolean skipSave = true;

	void loop(){
		try{
			engine.update(delta);
			world.update();
			checkQueue();
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Entity update loop error!");
			System.exit(1);
		}
	}
	
	//this is pretty terrible
	void checkQueue(){
		for(SendRequest r : sendQueue){
			r.life -= delta;
			if(r.life <= 0){
				server.sendToAll(r.object);
				sendQueue.remove(r);
			}
		}
	}
	
	public void addToSendQueue(Object object){
		sendQueue.add(new SendRequest(object));
	}

	void stop(){
		isRunning = false;
	}

	public float delta(){
		return delta;
	}

	public void run(){
		int fpsmillis = 1000 / maxfps;
		while(isRunning){
			long start = System.currentTimeMillis();
			loop();
			frameid++;
			long end = System.currentTimeMillis();

			try{
				if(end - start <= fpsmillis)
					Thread.sleep(fpsmillis - (end - start));
			}catch(Exception e){
				e.printStackTrace();
			}
			long sleepend = System.currentTimeMillis();
			delta = (sleepend - start) / 1000f * 60f;
		}
	}

	public KoruUpdater(KoruServer server) {
		this.server = server;
		file = new WorldFile(Paths.get("world"), new TerrainGenerator());
		world = new World(file);
		engine = new KoruEngine();
		engine.addSystem(new SyncSystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new InputSystem());

		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			saveAll();
		}));
	}

	Object lock = new Object();

	void saveAll(){
		if(skipSave){
			Koru.log("Skipping save.");
			return;
		}
		
		Koru.log("Saving " + file.getLoadedChunks().size() + " chunks...");
		
		totalchunks = file.getLoadedChunks().size();
		int pp = file.getLoadedChunks().size() / Runtime.getRuntime().availableProcessors();
		Koru.log("Chunks per thread: " + pp);
		int thread = 0;
		List<Chunk> threadchunks = new ArrayList<Chunk>();
		for(Chunk chunk : file.getLoadedChunks()){
			
			threadchunks.add(chunk);
			if(threadchunks.size() >= pp){
				Koru.log(ColorCodes.LIGHT_MAGENTA + "Spawning thread: " + ColorCodes.GREEN + thread);
				
				spawnChunkThread(threadchunks, thread++);
				threadchunks = new ArrayList<Chunk>();
			}

		}
		
		if(threads == 0) latch.countDown();
		
		try{
			latch.await();
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		Koru.log("Saving materials...");
		//I lied
		//MaterialManager.instance().saveMaterials(Directories.materials);
	}

	void spawnChunkThread(Collection<Chunk> chunks, int thread){
		threads ++;
		new Thread(() -> {
			for(Chunk chunk : chunks){
				Koru.log(ColorCodes.YELLOW + "Saving chunks: " +ColorCodes.RED + totalchunks + " left [Thread "+thread+"]");
				
				file.writeChunk(chunk, thread);
				totalchunks --;
			}
			
			if(--threads == 0) latch.countDown();
		}).start();
	}
	
	class SendRequest{
		Object object;
		float life = 4;
		
		public SendRequest(Object object){
			this.object = object;
		}
	}
}
