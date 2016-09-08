package net.pixelstatic.koru;

import net.pixelstatic.gdxutils.modules.ModuleController;
import net.pixelstatic.koru.modules.*;
import net.pixelstatic.koru.network.IClient;
import net.pixelstatic.koru.systems.InterpolationSystem;
import net.pixelstatic.koru.systems.KoruEngine;
import net.pixelstatic.koru.systems.RendererSystem;
import net.pixelstatic.koru.world.World;

import com.badlogic.gdx.Gdx;

public class Koru extends ModuleController<Koru>{
	private IClient client;
	private static Koru instance;
	public KoruEngine engine;
	
	public Koru(IClient client){
		this.client = client;
	}

	@Override
	public void init(){
		instance = this;
		engine = new KoruEngine();
		
		addModule(Network.class);
		addModule(Renderer.class);
		addModule(Input.class);
		addModule(ClientData.class);
		addModule(World.class);
		
		getModule(Network.class).client = client;

		engine.addSystem(new RendererSystem(getModule(Renderer.class)));

		engine.addSystem(new InterpolationSystem());
	}
	
	@Override
	public void render(){
		try{
			engine.update(Gdx.graphics.getDeltaTime());
			super.render();
		}catch (Exception e){
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	public static void log(Object o){
		if (Gdx.app == null)
			System.out.println("" + o);
		else
			Gdx.app.log("Koru:", "" + o);
		if(o instanceof Exception){
			((Exception)o).printStackTrace();
		}
	}
	
	public static KoruEngine getEngine(){
		return instance.engine;
	}
}
