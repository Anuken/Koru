package io.anuke.koru.generation;

public class GeneratedMaterialWrapper extends GeneratedMaterial{
	public GeneratedMaterialWrapper(GeneratedMaterial mat){
		super(mat.getType(), mat.id());
	}
	
	protected GeneratedMaterialWrapper(){}
	
	public GeneratedMaterial asMaterial(){
		return new GeneratedMaterial(getType(), id());
	}
}
