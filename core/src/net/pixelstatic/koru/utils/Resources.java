package net.pixelstatic.koru.utils;

import net.pixelstatic.gdxutils.graphics.Atlas;
import net.pixelstatic.koru.modules.Renderer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Resources{
	public static AtlasRegion findRegion(String name){
		return Renderer.i.atlas.findRegion(name);
	}
	
	public static Atlas getAtlas(){
		return Renderer.i.atlas;
	}
	
	public static BitmapFont getFont(){
		return Renderer.i.font;
	}
}
