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
	
	protected boolean checkData(Material material, Class<? extends TileData> data){
		if(material.getDataClass() == null){
			if(material.getType().tile()){
				tiledata = null;
			}else{
				blockdata = null;
			}
			return false;
		}
		if(material.getType().tile()){
			if(tiledata == null || !tiledata.getClass().getCanonicalName().equals(data.getCanonicalName())){
				tiledata = material.getDefaultData();
				return true;
			}
		}else{
			if(blockdata == null || !blockdata.getClass().getCanonicalName().equals(data.getCanonicalName())){
				blockdata = material.getDefaultData();
				return true;
			}
		}
		return false;
	}
	
//	public <T> T blockData(Class<T> c){
		//if(tiledata == null){
			
		//}
//	}
	
	public String toString(){
		return "Tile:[block="+block+" tile="+tile+ "]";
	}
}
