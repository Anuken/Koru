package net.pixelstatic.koru;

import java.util.ArrayList;
import java.util.HashMap;

import net.pixelstatic.koru.modules.*;
import net.pixelstatic.koru.systems.InterpolationSystem;
import net.pixelstatic.koru.systems.KoruEngine;
import net.pixelstatic.koru.systems.RendererSystem;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class Koru extends ApplicationAdapter{
	private static Koru instance;
	public HashMap<Class<? extends Module>, Module> modules = new HashMap<Class<? extends Module>, Module>();
	public ArrayList<Module> modulearray = new ArrayList<Module>();
	public KoruEngine engine;
	private static Thread thread;

	@Override
	public void create(){
		instance = this;
		engine = new KoruEngine();
		thread = Thread.currentThread();
		
		createModule(Network.class);
		createModule(Renderer.class);
		createModule(Input.class);
		createModule(ClientData.class);
		createModule(World.class);

		engine.addSystem(new RendererSystem(getModule(Renderer.class)));

		engine.addSystem(new InterpolationSystem());
		//engine.addSystem(new ProjectileRenderSystem());
		//engine.addSystem(new PlayerRenderSystem());
		//engine.addSystem(new IndicatorRenderSystem());

		for(Module m : modules.values())
			m.init();

	}

	@Override
	public void render(){
		try{
			engine.update(Gdx.graphics.getDeltaTime() * 60f);
			for(Module m : modulearray){
				m.update();
			}
		}catch(Exception e){
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	public static void log(Object o){
		System.out.println(o);
		if(o instanceof Exception){
			((Exception)o).printStackTrace();
		}
	}

	public static Thread getThread(){
		return thread;
	}
	
	public static KoruEngine getEngine(){
		return instance.engine;
	}

	public <T extends Module>T getModule(Class<T> c){
		return c.cast(modules.get(c));
	}

	public void createModule(Class<? extends Module> module){
		try{
			Module mod = module.getConstructor(this.getClass()).newInstance(this);
			modules.put(module, mod);
			modulearray.add(mod);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void resize(int width, int height){
		for(Module module : modules.values())
			module.resize(width, height);
	}
}
