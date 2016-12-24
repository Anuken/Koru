package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessorEffect;

public final class LightEffect extends PostProcessorEffect{

	private LightFilter light = null;

	public LightEffect(int viewportWidth, int viewportHeight) {
		setup(viewportWidth, viewportHeight);
	}

	private void setup(int viewportWidth, int viewportHeight){
		light = new LightFilter(viewportWidth, viewportHeight);
	}

	public void setSize(int width, int height){
		light.setSize(width, height);
	}

	public void setColor(float r, float g, float b, float a){
		light.setColor(r, g, b, a);
	}
	
	public void setColor(Color color){
		light.setColor(color.r, color.g, color.b, color.a);
	}

	@Override
	public void dispose(){
		light.dispose();
	}

	@Override
	public void rebind(){

	}

	@Override
	public void render(FrameBuffer src, FrameBuffer dest){
		restoreViewport(dest);
		light.setInput(src).setOutput(dest).render();
	}
}