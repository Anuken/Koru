package net.pixelstatic.koru.graphics;

import net.pixelstatic.koru.modules.Renderer;
import net.pixelstatic.koru.sprites.Layer;
import net.pixelstatic.koru.world.MaterialType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

public enum FrameBufferLayer{
	shadow("shadow", Layer.shadowlayer){
		public void end(){
			batch.setColor(Layer.shadowcolor);
			drawFull();
		}

		protected void begin(){

		}
	},
	reflection("reflection", Layer.reflectionlayer){
		public void end(){
			batch.setColor(new Color(1, 1, 1, 0.5f));
			drawFull();
		}

		protected void loadShader(){
			shader = ShaderLoader.fromFile("default", "reflection");
		}

		protected void begin(){
			
			shader.begin();
			shader.setUniformi("water", FrameBufferLayer.water.bind);
			//TextureRegion region = renderer.atlas().findRegion("water");
			//	shader.setUniformf("water", region.getRegionX(), region.getRegionY());
			///shader.setUniformf("cpos", camera.position.cpy().scl(7f));
			shader.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			//shader.setUniformf("time", Gdx.graphics.getFrameId()/60f);
			shader.end();
			
		}
	},
	water("water", MaterialType.tilelayer(-1)){
		public void end(){
			batch.setColor(new Color(1, 1, 1, 1f));
			drawFull();
		}

		protected void loadShader(){
			//	shader = ShaderLoader.fromFile("default", "water");
		}

		protected void begin(){
			/*
			shader.begin();
			shader.setUniformi("reflection", FrameBufferLayer.reflection.bind);
			//TextureRegion region = renderer.atlas().findRegion("water");
			//	shader.setUniformf("water", region.getRegionX(), region.getRegionY());
			///shader.setUniformf("cpos", camera.position.cpy().scl(7f));
			shader.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			//shader.setUniformf("time", Gdx.graphics.getFrameId()/60f);
			shader.end();
			*/
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

	abstract public void end();

	abstract protected void begin();

	public boolean layerEquals(float f){
		return MathUtils.isEqual(layer, f, 0.01f);
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
		ShaderLoader.BasePath = "shaders/";
		for(FrameBufferLayer layer : values()){
			layer.loadShader();
		}
	}
}
