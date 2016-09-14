package net.pixelstatic.koruserver;

import java.nio.file.Paths;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.systems.CollisionSystem;
import net.pixelstatic.koru.systems.KoruEngine;
import net.pixelstatic.koru.systems.SyncSystem;
import net.pixelstatic.koru.world.Chunk;
import net.pixelstatic.koru.world.MaterialManager;
import net.pixelstatic.koru.world.World;

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
	final MaterialManager materials = new MaterialManager();

	void Loop(){
		try{
			engine.update(delta);
			world.update();
			if(frameid % (blockupdatetime) == 0) updateTiles();
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Entity update loop error!");
			System.exit(1);
		}
	}

	void stop(){
		isRunning = false;
	}

	void updateTiles(){
		/*
		for(int x = 0; x < World.worldwidth; x ++){
			for(int y = 0; y < World.worldheight; y ++){
				Tile tile = world.tiles[x][y];
				if(tile.blockdata instanceof UpdatingTileData) ((UpdatingTileData)tile.blockdata).update(x,y,tile);
			}
		}
		*/
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
		file = new WorldFile(Paths.get("world"), new DefaultGenerator(world));
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
		Koru.log("Saving objects...");
		//Gdx.files.local(Variables.objectDirectory).writeString(Resources.getJson().toJson(materials.objects), false);
	}
}
