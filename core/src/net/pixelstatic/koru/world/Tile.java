package net.pixelstatic.koru.world;

public class Tile{
	public Material tile = Material.air;
	public Material block = Material.air;
	
	public Tile setTileMaterial(Material m){
		this.tile = m;
		return this;
	}
	
	public Tile setBlockMaterial(Material m){
		this.block = m;
		return this;
	}
	
	public void setMaterial(Material m){
		if(m.getType().tile()){
			tile = m;
		}else{
			block = m;
		}
	}
	
	public boolean solid(){
		return tile.getType().solid() || block.getType().solid();
	}
	
	public Material solidMaterial(){
		if(block.getType().solid()) return block;
		if(tile.getType().solid()) return tile;
		return null;
	}
	
	public String toString(){
		return "Tile:[block="+block+" tile="+tile+ "]";
	}
}
