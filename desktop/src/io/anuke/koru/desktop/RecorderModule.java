package io.anuke.koru.desktop;

import io.anuke.koru.Koru;
import io.anuke.ucore.modules.Module;

public class RecorderModule extends Module<Koru>{

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	/*
	public GifRecorder recorder = new GifRecorder(Renderer.i.batch, 1f/Renderer.i.GUIscale);
	
	@Override
	public void update(){
	
		if(Gdx.input.isKeyJustPressed(Keys.Q)){
			recorder.takeScreenshot();
		}
		
		recorder.update(Resources.findRegion("blank"), Gdx.graphics.getDeltaTime() * 60f);

	}
	*/
}
