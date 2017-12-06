package io.anuke.koru.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.graphics.Shader;
import io.anuke.ucore.util.Tmp;

public class Shaders{
	public static final Outline outline = new Outline();
	public static final Inline inline = new Inline();
	public static final Water water = new Water();
	public static final Mix mix = new Mix();
	public static final Round round = new Round();
	public static final Light light = new Light();
	
	public static class Water extends Shader{
		
		public Water(){
			super("water", "default");
		}

		@Override
		public void apply(){
			shader.setUniformf("camerapos", Core.camera.position.x, Core.camera.position.y);
			shader.setUniformf("screensize", Gdx.graphics.getWidth() / Core.cameraScale, Gdx.graphics.getHeight() / Core.cameraScale);
			shader.setUniformf("time", Timers.time());
		}
	}
	
	public static class Light extends Shader{
		
		public Light(){
			super("light", "default");
		}

		@Override
		public void apply(){
			
		}
	}
	
	public static class Round extends Shader{
		
		public Round(){
			super("round", "default");
		}

		@Override
		public void apply(){
			
		}
	}
	
	public static class Mix extends Shader{
		public Color color = new Color();
		public float amount = 0.5f;
		
		public Mix(){
			super("mix", "default");
		}

		@Override
		public void apply(){
			shader.setUniformf("tint", color);
			shader.setUniformf("amount", amount);
		}
	}
	
	public static class Outline extends Shader{
		public Color color = Color.WHITE;
		
		public Outline(){
			super("outline", "outline");
		}

		@Override
		public void apply(){
			shader.setUniformf("u_texsize", Tmp.v1.set(region.getTexture().getWidth(), region.getTexture().getHeight()));
			shader.setUniformf("u_color", color);
		}
	}
	
	public static class Inline extends Shader{
		public Color color = Color.WHITE;
		
		public Inline(){
			super("inline", "outline");
		}

		@Override
		public void apply(){
			shader.setUniformf("u_texsize", Tmp.v1.set(region.getTexture().getWidth(), region.getTexture().getHeight()));
			shader.setUniformf("u_size", Tmp.v1.set(region.getRegionWidth(), region.getRegionHeight()));
			shader.setUniformf("u_pos", Tmp.v1.set(region.getRegionX(), region.getRegionY()));
			shader.setUniformf("u_color", color);
		}
	}
}
