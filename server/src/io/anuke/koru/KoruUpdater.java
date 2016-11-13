package io.anuke.koru;

import java.nio.file.Paths;

import io.anuke.koru.generation.MaterialManager;
import io.anuke.koru.systems.CollisionSystem;
import io.anuke.koru.systems.KoruEngine;
import io.anuke.koru.systems.SyncSystem;
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
			frameid ++;
			long end = System.currentTimeMillis();

			try{
				if(end - start <= fpsmillis) Thread.sleep(fpsmillis - (end - start));
			}catch(Exception e){
				e.printStackTrace();
			}
			long sleepend = System.currentTimeMillis();
			delta = (sleepend - start) / 1000f * 60f;
		}

	}

	public KoruUpdater(KoruServer server){
		this.server = server;
		file = new WorldFile(Paths.get("world"), new SimpleGenerator());
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

	void saveAll(){
		Koru.log("Saving " + file.getLoadedChunks().size() + " chunks...");
		for(Chunk chunk : file.getLoadedChunks()){
			file.writeChunk(chunk);
		}
		Koru.log("Saving materials...");
		MaterialManager.instance().saveMaterials(Directories.materials);
	}
}
