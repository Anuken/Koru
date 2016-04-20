package net.pixelstatic.koru.server;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.systems.*;
import net.pixelstatic.koru.world.Generator;

public class KoruUpdater{
	public static KoruUpdater instance;
	KoruServer server;
	KoruEngine engine;
	public World world;
	Generator generator;
	private boolean isRunning = true;
	final int maxfps = 60;
	static long frameid;
	float delta = 1f;
	long lastFpsTime;

	void Loop(){
		try{
			engine.update(delta);
			if(frameid % (60*5) == 0) updateTiles();
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Entity update loop error!");
			System.exit(1);
		}
	}
	
	void updateTiles(){
		
	}

	public static long frameID(){
		return frameid;
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
		instance = this;
		this.server = server;
		world = new World(server, true);
		generator = new Generator(world);
		generator.generate();
		engine = new KoruEngine();
		engine.addSystem(new SyncSystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new BehaviorSystem());

		for(int i = 0;i < 20;i ++){
			KoruEntity entity = new KoruEntity(EntityType.testmonster).addSelf();
			entity.position().set(20f, 20f);
		}
	}
}
