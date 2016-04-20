package net.pixelstatic.koru.world;

public class MaterialPair{
	public Material block, tile;
	
	public MaterialPair(Material block, Material tile){
		set(block, tile);
	}
	
	public MaterialPair(){
		
	}
	
	public void set(Material block, Material tile){
		this.block = block;
		this.tile = tile;
	}
	
	public void clear(){
		set(null, null);
	}
}
