package io.anuke.koru.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.bitfire.postprocessing.PostProcessor;

import io.anuke.ucore.graphics.CustomSurface;

public class ProcessorSurface extends CustomSurface{
	private LightEffect light;
	private PostProcessor processor;
	
	public ProcessorSurface() {
		super("processor");
	}
	
	public void setLightColor(Color color){
		//TODO fix light shadow bugs
		light.setColor(color);
	}
	
	private void addEffects(){
		
	}
	
	@Override
	public void begin(boolean clear){
		if(clear){
			processor.capture();
		}else{
			processor.captureNoClear();
		}
	}
	
	@Override
	public void end(){
		processor.render();
	}
	
	@Override
	public void resize(){
		if(processor != null)
			processor.dispose();
		processor = new PostProcessor(false, true, true);
		
		if(light != null)
			light.dispose();
		light = new LightEffect(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//processor.addEffect(light);
	}
	
	@Override
	public void dispose(){
		processor.dispose();
	}

}
