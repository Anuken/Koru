package net.pixelstatic.koru.world;


public class Tile{
	public Material tile = Material.air;
	public Material block = Material.air;
	public TileData tiledata, blockdata;
	
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
	
	public <T> T getBlockData(Class<T> c){
		return c.cast(blockdata);
	}
	
	public void changeEvent(){
		if(invalidData(tile, tiledata)){
			tiledata = tile.getDefaultData();
		}
		
		if(invalidData(block, blockdata)){
			blockdata = block.getDefaultData();
		}
		tile.changeEvent(this);
		block.changeEvent(this);
	}
	
	public boolean invalidData(Material material, TileData data){
		return material.getDataClass() == null || data == null || !material.getDataClass().getCanonicalName().equals(data.getClass().getCanonicalName());
	}
	
	
	public String toString(){
		return "Tile:[block="+block+" tile="+tile+ "]";
	}
}
