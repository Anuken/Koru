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
		light.setColor(color);
	}
	
	private void addEffects(){
		if(light != null)
			light.dispose();
		light = new LightEffect(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		processor.addEffect(light);
	}
	
	public void render(){
		processor.render();
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
	public void end(boolean render){
		if(render){
			processor.render();
		}else{
			processor.captureEnd();
		}
	}
	
	@Override
	public void resize(){
		if(processor != null)
			processor.dispose();
		processor = new PostProcessor(false, true, true);
		
		addEffects();
	}
	
	@Override
	public void dispose(){
		processor.dispose();
	}

}
