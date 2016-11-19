package io.anuke.koru.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Json;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.Renderer;

public class Resources{
	private static Json json;
	
	public static AtlasRegion region(String name){
		return Koru.module(Renderer.class).atlas.findRegion(name);
	}
	
	public static RepackableAtlas atlas(){
		return Koru.module(Renderer.class).atlas;
	}
	
	public static BitmapFont font(){
		return Koru.module(Renderer.class).font;
	}
	
	public static Json getJson(){
		if(json == null) json = new Json();
		return json;
	}
}
