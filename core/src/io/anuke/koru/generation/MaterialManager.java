package io.anuke.koru.generation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.Koru;
import io.anuke.koru.network.IServer;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.utils.Text;
import io.anuke.koru.world.MaterialType;

public class MaterialManager{
	private static MaterialManager instance = new MaterialManager();
	private int lastMaterialID;
	private ObjectMap<Integer, GeneratedMaterial> objects = new ObjectMap<Integer, GeneratedMaterial>();

	private MaterialManager() {
	}

	public static MaterialManager instance(){
		return instance;
	}

	public GeneratedMaterial getMaterial(int id){
		return objects.get(id);
	}

	public GeneratedMaterial createMaterial(MaterialType type){
		GeneratedMaterial mat = new GeneratedMaterial(type, lastMaterialID++);
		objects.put(mat.id(), mat);

		return mat;
	}

	public void saveMaterials(FileHandle file){
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
			if(objects.size != 0)lastMaterialID++;
		}catch(Exception e){
			Koru.log("Material file corrupted or not found.");
		}
		
		Koru.log("Successfuly loaded "+objects.size + " generated materials.");
	}
}
