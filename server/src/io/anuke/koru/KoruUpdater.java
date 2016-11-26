package io.anuke.koru;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.anuke.koru.systems.CollisionSystem;
import io.anuke.koru.systems.KoruEngine;
import io.anuke.koru.systems.SyncSystem;
import io.anuke.koru.utils.Text;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.World;

public class KoruUpdater{
	KoruServer server;
	public KoruEngine engine;
	public World world;
	public final WorldFile file;
	private boolean isRunning = true;
	final int maxfps = 60;
	long frameid;
	float delta = 1f;
	long lastFpsTime;
	final int blockupdatetime = 60 * 6;
	CountDownLatch latch = new CountDownLatch(1);
	int threads = 0;
	int totalchunks = 0;
	boolean skipSave = true;

	void Loop(){
		try{
			engine.update(delta);
			world.update();
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Entity update loop error!");
			System.exit(1);
		}
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
			Loop();
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

		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				saveAll();
			}
		});
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
				Koru.log(Text.LIGHT_MAGENTA + "Spawning thread: " + Text.GREEN + thread);
				
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
		// MaterialManager.instance().saveMaterials(Directories.materials);
	}

	void spawnChunkThread(Collection<Chunk> chunks, int thread){
		threads ++;
		new Thread(() -> {
			for(Chunk chunk : chunks){
				Koru.log(Text.YELLOW + "Saving chunks: " +Text.RED + totalchunks + " left [Thread "+thread+"]");
				
				file.writeChunk(chunk, thread);
				totalchunks --;
			}
			
			if(--threads == 0)latch.countDown();
		}).start();
	}
}
