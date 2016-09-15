package io.anuke.koru;

import io.anuke.koru.modules.*;
import io.anuke.koru.network.IClient;
import io.anuke.koru.systems.InterpolationSystem;
import io.anuke.koru.systems.KoruEngine;
import io.anuke.koru.systems.RendererSystem;
import io.anuke.koru.world.World;
import net.pixelstatic.gdxutils.modules.ModuleController;

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
