package net.pixelstatic.koru.graphics;

import net.pixelstatic.koru.sprites.Layer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public enum FrameBufferLayer{
	shadow("shadow", Layer.shadowlayer){
		public void end(){
			batch.setColor(Layer.shadowcolor);
			batch.draw(fbo.getColorBufferTexture(), camera.position.x - camera.viewportWidth / 2*camera.zoom, camera.position.y + camera.viewportHeight / 2*camera.zoom, camera.viewportWidth*camera.zoom, -camera.viewportHeight*camera.zoom);
		}

		protected void begin(){
			// TODO Auto-generated method stub
			
		}
	};
	public final String name;
	public final float layer;
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	protected FrameBuffer fbo;
	
	private FrameBufferLayer(String name, float layer){
		this.name = name;
		this.layer = layer;
	}

	
	abstract public void end();
	abstract protected void begin();
	public void beginDraw(SpriteBatch batch, OrthographicCamera camera, FrameBuffer fbo){
		this.batch = batch;
		this.fbo = fbo;
		this.camera = camera;
		begin();
	}
}
