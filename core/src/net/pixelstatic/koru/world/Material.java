package net.pixelstatic.koru.world;

public enum Material{
	air, 
	grass, 
	stone, 
	pinetree, 
	stoneblock(MaterialType.block);
	
	private MaterialType type = MaterialType.tile;
	
	private Material(){
		
	}
	
	private Material(MaterialType type){
		this.type = type;
	}
	
	public MaterialType getType(){
		return type;
	}
}
