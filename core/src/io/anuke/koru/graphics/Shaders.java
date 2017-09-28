package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.graphics.Shader;
import io.anuke.ucore.util.Tmp;

public class Shaders{
	
	public static void create(){
		new Outline();
		new Inline();
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
