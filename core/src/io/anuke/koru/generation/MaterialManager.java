package io.anuke.koru.generation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.ObjectHandler;
import io.anuke.koru.network.IServer;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.utils.Text;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;
import io.anuke.koru.world.Materials;

/**Materials IDs < 0 are generated.*/
public class MaterialManager{
	private static final MaterialManager instance = new MaterialManager();
	private static final Materials[] values = Materials.values();
	private int goffset = Integer.MIN_VALUE;
	private int lastMaterialID = goffset;
	private Array<GeneratedMaterial> genMaterials = new Array<GeneratedMaterial>();
	//private ObjectMap<Integer, GeneratedMaterial> genMaterials = new ObjectMap<>();
	//private ObjectMap<Integer, GeneratedMaterial> objects = new ObjectMap<Integer, GeneratedMaterial>();
	

	private MaterialManager() {}

	public static MaterialManager instance(){
		return instance;
	}
	
	public Material getMaterial(int id){
		if(id < 0){
			GeneratedMaterial mat = goffset+id >= genMaterials.size ? null : genMaterials.get(goffset+id);
			//if it's a client and the material is being loaded, return air
			if(mat == null && !IServer.active()){
				ObjectHandler.instance().notifyMaterialUnknown(id);
				return values[0];
			}else if(mat == null){
				Koru.log(Text.RED+"Unknown material with ID "+ Text.LIGHT_CYAN+ id + Text.RED + ". Exiting.");
				System.exit(-1);
			}
			return mat;
		}
		return values[id];
	}
	
	public GeneratedMaterial getGeneratedMaterial(int id){
		return (GeneratedMaterial) getMaterial(id+goffset);
	}
	
	public int generatedMaterialSize(){
		return genMaterials.size;
	}
	
	/**Client-side only.*/
	public void registerMaterial(GeneratedMaterial mat){
		genMaterials.setSize(mat.id()+goffset+1);
		genMaterials.set(mat.id()+goffset, mat);
	}

	public GeneratedMaterial createMaterial(MaterialType type){
		GeneratedMaterial mat = new GeneratedMaterial(type, lastMaterialID++);
		registerMaterial(mat);
		return mat;
	}

	public void saveMaterials(FileHandle file){
		Koru.log("Saving " + genMaterials.size +" materials...");
		Resources.getJson().toJson(genMaterials, file);
	}

	/**
	 * Not thread safe. Must be called with OpenGL context as pixmaps are
	 * created.
	 */
	@SuppressWarnings("unchecked")
	public void loadMaterials(FileHandle file){
		try{
			genMaterials = Resources.getJson().fromJson(Array.class, file);
			for(GeneratedMaterial material : genMaterials){
				if(material == null) continue;
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
			}
			if(genMaterials.size != 0) lastMaterialID++;
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Material file corrupted or not found.");
			genMaterials = new Array<GeneratedMaterial>();
		}
		
		Koru.log("Successfuly loaded " + genMaterials.size + " generated materials.");
	}
}
