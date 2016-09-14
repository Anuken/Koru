package net.pixelstatic.koru.world;

import com.badlogic.gdx.utils.ObjectMap;

public class MaterialManager{
	public ObjectMap<Integer, Material> objects = new ObjectMap<Integer, Material>();
	
	public Material getMaterial(int id){
		return objects.get(id);
	}
}
