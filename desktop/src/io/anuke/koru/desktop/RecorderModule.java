package io.anuke.koru.desktop;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.Renderer;
import io.anuke.koru.utils.Resources;
import net.pixelstatic.gdxutils.modules.Module;
import net.pixelstatic.utils.io.GifRecorder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class RecorderModule extends Module<Koru>{
	public GifRecorder recorder = new GifRecorder(Renderer.i.batch, 1f/Renderer.i.GUIscale);
	
	@Override
	public void update(){
		if(Gdx.input.isKeyJustPressed(Keys.Q)){
			recorder.takeScreenshot();
		}
		
		recorder.update(Resources.findRegion("blank"), Gdx.graphics.getDeltaTime() * 60f);

	}
}
