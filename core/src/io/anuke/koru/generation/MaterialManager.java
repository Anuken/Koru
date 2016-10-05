package io.anuke.koru.generation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.Koru;
import io.anuke.koru.network.IServer;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.utils.Text;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;
import io.anuke.koru.world.Materials;

/**Materials IDs < 0 are generated.*/
public class MaterialManager{
	private static MaterialManager instance = new MaterialManager();
	private int lastMaterialID = Integer.MIN_VALUE;
	private ObjectMap<Integer, GeneratedMaterial> objects = new ObjectMap<Integer, GeneratedMaterial>();
	private Materials[] values = Materials.values();

	private MaterialManager() {}

	public static MaterialManager instance(){
		return instance;
	}
	
	public Material getMaterial(int id){
		if(id < 0){
			return objects.get(id);
		}
		return values[id];
	}

	public GeneratedMaterial getGeneratedMaterial(int id){
		return objects.get(id);
	}
	
	/**Client-side only.*/
	public void registerMaterial(GeneratedMaterial mat){
		objects.put(mat.id(), mat);
	}

	public GeneratedMaterial createMaterial(MaterialType type){
		GeneratedMaterial mat = new GeneratedMaterial(type, lastMaterialID++);
		objects.put(mat.id(), mat);
		return mat;
	}

	public void saveMaterials(FileHandle file){
		Koru.log("Saving " + objects.size +" materials...");
		Resources.getJson().toJson(objects, file);
	}

	/**
	 * Not thread safe. Must be called with OpenGL context as pixmaps are
	 * created.
	 */
	@SuppressWarnings("unchecked")
	public void loadMaterials(FileHandle file){
		try{
			objects = Resources.getJson().fromJson(ObjectMap.class, file);
			for(GeneratedMaterial material : objects.values()){
				lastMaterialID = Math.max(material.id(), lastMaterialID);
				try{
					if(IServer.active()){
						material.loadPixmap();
					}else{
						material.loadTexture();
					}
				}catch(Exception e){
					Koru.log(Text.BACK_RED + "Failure loading custom material: " + material.name + Text.BACK_DEFAULT);
					e.printStackTrace();
				}
				objects.put(material.id(), material);
			}
			if(objects.size != 0) lastMaterialID++;
		}catch(Exception e){
			Koru.log("Material file corrupted or not found.");
		}
		
		Koru.log("Successfuly loaded "+objects.size + " generated materials.");
	}
}
