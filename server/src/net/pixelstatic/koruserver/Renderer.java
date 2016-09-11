package net.pixelstatic.koruserver;

import net.pixelstatic.fluxe.generation.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;

public class Renderer extends ApplicationAdapter{
	Crux crux;
	Fluxor flux;

	public void create(){
		crux = new Crux();
		flux = new Fluxor(new TreeVoxelizer(), new DefaultRasterizer());

		Lwjgl3Graphics g = (Lwjgl3Graphics)(Gdx.graphics);
		g.getWindow().iconifyWindow();
		Gdx.graphics.setContinuousRendering(false);

	}

	public void render(){
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
		
		if(Gdx.input.isKeyJustPressed(Keys.E)) crux.render(flux);
	}
}
