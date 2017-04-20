package io.anuke.koru.graphics;

import static io.anuke.ucore.core.Mathf.clamp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Align;

import io.anuke.koru.utils.Resources;
import io.anuke.ucore.graphics.ShapeUtils;


public class Draw{
	
	public static void shader(ShaderProgram shader, Object...params){
		boolean rendering = batch().isDrawing();
		
		if(rendering)
			batch().end();
		
		if(shader != null)
		Shaders.setParams(shader, params);
		
		batch().setShader(shader);
		
		if(rendering)
			batch().begin();
	}
	
	public static void rect(String name, float x, float y){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x - region.getRegionWidth()/2, y - region.getRegionHeight()/2);
	}
	
	/**Grounded rect*/
	public static void grect(String name, float x, float y){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x - region.getRegionWidth()/2, y);
	}
	
	/**Grounded rect*/
	public static void grect(String name, float x, float y, float w, float h){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x - region.getRegionWidth()/2, y, w, h);
	}
	
	/**Grounded rect*/
	public static void grect(String name, float x, float y, float rotation){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x - region.getRegionWidth()/2, y, region.getRegionWidth()/2, region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);
	}
	
	public static void rect(String name, float x, float y, float rotation){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x - region.getRegionWidth()/2, y - region.getRegionHeight()/2, region.getRegionWidth()/2, region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);
	}
	
	public static void rect(String name, float x, float y, float w, float h){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x - w/2, y - h/2, w, h);
	}
	
	public static void crect(String name, float x, float y, float w, float h){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x, y, w, h);
	}
	
	public static void crect(String name, float x, float y){
		TextureRegion region = Resources.region(name);
		Resources.batch().draw(region, x, y);
	}
	
	public static void crect(TextureRegion region, float x, float y){
		Resources.batch().draw(region, x, y);
	}
	
	public static void linerect(float x, float y, float w, float h){
		ShapeUtils.rect(batch(), x, y, w, h, 1);
	}
	
	public static void line(float x, float y, float x2, float y2){
		ShapeUtils.line(batch(), x, y, x2, y2);
	}
	
	public static void color(String hex){
		batch().setColor(Color.valueOf(hex));
	}
	
	public static void color(Color color){
		batch().setColor(color);
	}
	
	public static void color(float r, float g, float b){
		batch().setColor(clamp(r), clamp(g), clamp(b), 1f);
	}
	
	/**Lightness color.*/
	public static void colorl(float l){
		color(l, l, l);
	}
	
	/**Lightness color, alpha.*/
	public static void colorl(float l, float a){
		color(l, l, l, a);
	}
	
	public static void color(float r, float g, float b, float a){
		batch().setColor(clamp(r), clamp(g), clamp(b), clamp(a));
	}
	
	/**Alpha color.*/
	public static void color(float a){
		batch().setColor(1f, 1f, 1f, a);
	}
	
	/**Resets the color to white.*/
	public static void color(){
		batch().setColor(Color.WHITE);
	}
	
	public static void tcolor(Color color){
		Resources.font().setColor(color);
	}
	
	/**Alpha color.*/
	public static void tcolor(float alpha){
		Resources.font().setColor(1f, 1f, 1f, alpha);
	}
	
	public static void tcolor(){
		Resources.font().setColor(Color.WHITE);
	}
	
	public static void text(String text, float x, float y){
		text(text, x, y, Align.center);
	}
	
	public static void text(String text, float x, float y, int align){
		Resources.font().draw(batch(), text, x, y, 0, align, false);
	}
	
	public static Batch batch(){
		return Resources.batch();
	}
}
