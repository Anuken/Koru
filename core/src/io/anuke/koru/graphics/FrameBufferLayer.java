package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.Renderer;
import io.anuke.ucore.spritesystem.Renderable;

public enum FrameBufferLayer{
	shadow("shadow", Layers.shadow){
		public void end(){
			batch.setColor(0,0,0,0.1f);
			drawFull();
		}
	}, 
	light("light", Layers.light){
		{
			bind = 6;
		}
	},
	darkness("darkness", Layers.dark){
		{
			bind = 7;
		}
	};

	public final String name;
	public final float layer;
	public int bind = ordinal() + 1;
	public ShaderProgram shader;
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	protected FrameBuffer fbo;
	protected Renderer renderer;

	private FrameBufferLayer(String name, float layer){
		this.name = name;
		this.layer = layer;
	}

	public void end(){}

	protected void begin(){}

	public boolean layerEquals(Renderable l){
		return MathUtils.isEqual(l.getLayer(), layer, 0.01f);
	}

	protected void loadShader(){

	}

	protected void drawFull(){
		batch.draw(fbo.getColorBufferTexture(), camera.position.x - camera.viewportWidth / 2 * camera.zoom, camera.position.y + camera.viewportHeight / 2 * camera.zoom, camera.viewportWidth * camera.zoom, -camera.viewportHeight * camera.zoom);
	}

	public void beginDraw(Renderer renderer, SpriteBatch batch, OrthographicCamera camera, FrameBuffer fbo){
		this.batch = batch;
		this.fbo = fbo;
		this.camera = camera;
		this.renderer = renderer;
		begin();
	}

	public static void loadShaders(){
		Koru.log("Loading shaders...");
		
		Shaders.loadAll();
		
		for(FrameBufferLayer layer : values()){
			layer.loadShader();
		}
	}
}
