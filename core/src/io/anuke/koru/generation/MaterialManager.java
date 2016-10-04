package io.anuke.koru.generation;

import com.badlogic.gdx.utils.ObjectMap;

public class MaterialManager{
	private static MaterialManager instance = new MaterialManager();
	public ObjectMap<Integer, GeneratedMaterial> objects = new ObjectMap<Integer, GeneratedMaterial>();
	
	private MaterialManager(){}
	
	public static MaterialManager instance(){
		return instance;
	}
	
	public GeneratedMaterial getMaterial(int id){
		return objects.get(id);
	}
	
	public void registerMaterial(GeneratedMaterial mat){
		objects.put(mat.id(), mat);
	}
}
