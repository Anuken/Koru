package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public class Layers{
	public static final float light = 999999;
	public static final float dark = 999999+1;
	public static final float shadow = -999999;
	
	private static ObjectMap<String, TextureRegion[]> regions = new ObjectMap<>();
	
	public static TextureRegion[] get(String name){
		if(!regions.containsKey(name)){
			TextureRegion[] regions = null;
			
			Texture tex = new Texture("layers/" + name + ".png");
			regions = TextureRegion.split(tex, tex.getHeight(), tex.getHeight())[0];
			
			Layers.regions.put(name, regions);
			return regions;
		}else{
			return regions.get(name);
		}
	}
}
