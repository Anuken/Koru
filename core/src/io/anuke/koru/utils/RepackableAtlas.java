package io.anuke.koru.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.graphics.Atlas;

public class RepackableAtlas extends Atlas{
	private ObjectMap<String, AtlasRegion> extraRegions = new ObjectMap<String, AtlasRegion>();
	
	public RepackableAtlas(FileHandle file){
		super(file);
	}
	
	public void addTexture(String name, Texture texture){
		extraRegions.put(name, new AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight()));
	}

	public AtlasRegion findRegion(String name){
		if(extraRegions != null && extraRegions.containsKey(name)){
			return extraRegions.get(name);
		}
		return super.findRegion(name);
	}
}
