package io.anuke.koru;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Pixmap;

public class Renderer extends ApplicationAdapter{
	//Crux crux;
	//Fluxor flux;

	public void create(){
		//crux = new Crux();
		//flux = new Fluxor(new TreeVoxelizer(), new DefaultRasterizer());

		Lwjgl3Graphics g = (Lwjgl3Graphics)(Gdx.graphics);
		g.getWindow().iconifyWindow();
		Gdx.graphics.setContinuousRendering(false);

	}

	public void render(){
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
		
		if(Gdx.input.isKeyJustPressed(Keys.E)){
			//Pixmap pixmap = crux.render(flux);
			//PixmapIO.writePNG(Gdx.files.local("tree.png"), pixmap);
		}
	}
	
	public Pixmap generate(){
		return null;
		//return crux.render(flux);
	}
}
