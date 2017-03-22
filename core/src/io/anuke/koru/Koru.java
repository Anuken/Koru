package io.anuke.koru;

import java.util.Calendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.koru.modules.*;
import io.anuke.koru.network.IServer;
import io.anuke.koru.systems.CollisionDebugSystem;
import io.anuke.koru.systems.KoruEngine;
import io.anuke.koru.utils.Profiler;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.UCore;
import io.anuke.ucore.modules.ModuleController;
import io.anuke.ucore.util.ColorCodes;
import io.anuke.ucore.util.Timers;

public class Koru extends ModuleController<Koru>{
	private static Koru instance;
	private static StringBuffer log = new StringBuffer();
	public KoruEngine engine;

	@Override
	public void init(){
		Resources.loadMaterials();
		
		instance = this;
		engine = new KoruEngine();

		addModule(Network.class);
		addModule(Renderer.class);
		addModule(Input.class);
		addModule(ClientData.class);
		addModule(World.class);
		addModule(UI.class);
		
		engine.addSystem(new CollisionDebugSystem());
	}

	@Override
	public void render(){
		
		Timers.update(delta());
		
		try{
			long start = TimeUtils.nanoTime();
			
			engine.update(Gdx.graphics.getDeltaTime()*60f);
			
			if(Profiler.update())
			Profiler.engineTime = TimeUtils.timeSinceNanos(start);
			
			long mstart = TimeUtils.nanoTime();
			
			super.render();
			
			if(Profiler.update()){
				Profiler.moduleTime = TimeUtils.timeSinceNanos(mstart);
				Profiler.totalTime = TimeUtils.timeSinceNanos(start);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
			//write log
			if(!System.getProperty("user.name").equals("anuke"))
			Gdx.files.local("korulog-" + Calendar.getInstance().getTime() + ".log").writeString(UCore.parseException(e), false);
			//exit, nothing left to do here
			Gdx.app.exit();
		}

	}
	
	public static float delta(){
		return IServer.active() ? IServer.instance().getDelta() : Gdx.graphics.getDeltaTime()*60f;
	}

	public static void log(Object o){
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		String name = e.getFileName().replace(".java", "");

		if(IServer.active() || Gdx.app == null){
			if(Gdx.app == null){
				System.out.println(ColorCodes.BACK_DEFAULT + ColorCodes.BOLD + ColorCodes.LIGHT_BLUE + "[" + name + "]: "
						+ ColorCodes.LIGHT_GREEN + o + ColorCodes.RED);
			}else{
				Gdx.app.log(
						ColorCodes.BACK_DEFAULT + ColorCodes.LIGHT_BLUE + "[" + ColorCodes.BLUE + name + ColorCodes.BACK_DEFAULT + "::"
								+ ColorCodes.LIGHT_YELLOW + e.getMethodName() + ColorCodes.LIGHT_BLUE + "]",
						ColorCodes.LIGHT_GREEN + "" + o + ColorCodes.RED);
			}
		}else{
			Gdx.app.log("[" + name + "::" + e.getMethodName() + "]", "" + o);

			String message = "[" + name + "::" + e.getMethodName() + "]" + "" + o;
			log.append(message + "\n");
			int l = 500;
			
			
			log = new StringBuffer(log.substring(Math.max(0, log.length()-l), log.length()));
		}

		if(o instanceof Exception){
			((Exception) o).printStackTrace();
		}

	}
		
	public static CharSequence getLog(){
		return log;
	}

	public static KoruEngine getEngine(){
		return instance.engine;
	}
}
