package io.anuke.koru;

import java.util.Calendar;

import com.badlogic.gdx.Gdx;

import io.anuke.koru.graphics.lsystems.LSystems;
import io.anuke.koru.modules.*;
import io.anuke.koru.network.Net;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.ecs.Basis;
import io.anuke.ucore.ecs.extend.processors.TileCollisionProcessor;
import io.anuke.ucore.modules.ModuleCore;
import io.anuke.ucore.util.ColorCodes;
import io.anuke.ucore.util.Strings;

public class Koru extends ModuleCore{
	public static Renderer renderer;
	public static Control control;
	public static UI ui;
	public static World world;
	public static Network network;
	
	public static Basis basis;
	
	private static StringBuffer log = new StringBuffer();

	@Override
	public void init(){
		LSystems.generate();
		try{
			
			Resources.loadData();
			
			basis = new Basis();
			
			basis.addProcessor(new TileCollisionProcessor(World.tilesize, (x, y)->{
				Tile tile = world.getTile(x, y);
				return tile != null && tile.solid();
			}, (x, y, out)->{
				Tile tile = world.getTile(x, y);
				tile.solidMaterial().getHitbox(x, y, out);
			}));
			
			Effects.setEffectProvider((id, color, x, y, rotation)->{
				throw new IllegalArgumentException("Effects cannot be created clientside!");
			});
	
			module(network = new Network());
			module(control = new Control());
			module(renderer = new Renderer());
			
			module(world = new World());
			module(ui = new UI());
			
		}catch (Exception e){
			handleException(e);
		}
		
		//engine.addSystem(new CollisionDebugSystem());
	}

	@Override
	public void render(){
		
		Timers.update();
		
		try{
			super.render();
			basis.update();
		}catch(Exception e){
			handleException(e);
		}
	}
	
	static void handleException(Exception e){
		e.printStackTrace();
		
		//write log - useless for me, since I can just see the stack trace
		if(!System.getProperty("user.name").equals("anuke")){
			Gdx.files.local("korulog-" + Calendar.getInstance().getTime() + ".log").writeString(Strings.parseException(e), false);
		}
		//exit, nothing left to do here
		Gdx.app.exit();
	}

	public static void log(Object o){
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		String name = e.getFileName().replace(".java", "");

		if(Net.server() || Gdx.app == null){
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
}
