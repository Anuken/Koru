package io.anuke.koru;

import com.badlogic.gdx.Gdx;

import io.anuke.koru.modules.ClientData;
import io.anuke.koru.modules.Input;
import io.anuke.koru.modules.Network;
import io.anuke.koru.modules.ObjectHandler;
import io.anuke.koru.modules.Renderer;
import io.anuke.koru.modules.UI;
import io.anuke.koru.network.IClient;
import io.anuke.koru.network.IServer;
import io.anuke.koru.systems.InterpolationSystem;
import io.anuke.koru.systems.KoruEngine;
import io.anuke.koru.systems.RendererSystem;
import io.anuke.koru.utils.Text;
import io.anuke.koru.world.World;
import io.anuke.ucore.modules.ModuleController;

public class Koru extends ModuleController<Koru>{
	private IClient client;
	private static Koru instance;
	public KoruEngine engine;

	public Koru(IClient client) {
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
		addModule(ObjectHandler.class);
		addModule(UI.class);

		getModule(Network.class).client = client;

		engine.addSystem(new RendererSystem(getModule(Renderer.class)));

		engine.addSystem(new InterpolationSystem());
	}

	@Override
	public void render(){
		try{
			engine.update(Gdx.graphics.getDeltaTime());
			super.render();
		}catch(Exception e){
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public static <T> T module(Class<T> c){
		return instance.getModule(c);
	}

	public static void log(Object o){
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		String name = e.getFileName().replace(".java", "");

		if(IServer.active() || Gdx.app == null){
			if(Gdx.app == null){
				System.out.println(
						Text.BACK_DEFAULT + Text.BOLD + Text.LIGHT_BLUE + "[" + name + "]: " + Text.LIGHT_GREEN + o + Text.RED);
			}else{
				Gdx.app.log(
						Text.BACK_DEFAULT + Text.LIGHT_BLUE + "[" + Text.BLUE + name + Text.BACK_DEFAULT + "::"
								+ Text.LIGHT_YELLOW + e.getMethodName() + Text.LIGHT_BLUE + "]",
						Text.LIGHT_GREEN + "" + o + Text.RED);
			}
		}else{
			Gdx.app.log("[" + name + "::" + e.getMethodName() + "]", "" + o);
		}

		if(o instanceof Exception){
			((Exception) o).printStackTrace();
		}
	}

	public static KoruEngine getEngine(){
		return instance.engine;
	}
}
