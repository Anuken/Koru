package net.pixelstatic.koru.world;

import com.badlogic.gdx.utils.Pool.Poolable;

public class Tile implements Poolable{
	public Material tile = Materials.air;
	public Material block = Materials.air;
	public TileData tiledata, blockdata;
	
	public Tile(){}
	
	public Tile(Material tile, Material block){
		this.tile = tile;
		this.block = block;
	}

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

	@SuppressWarnings("unchecked")
	public <T>T getBlockData(Class<T> c){
		return (T)(blockdata);
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
		return material.getDataClass() == null || data == null || !material.getDataClass().equals(data.getClass());
	}

	public String toString(){
		return "Tile:[block=" + block + " tile=" + tile + "]";
	}

	@Override
	public void reset(){
		tile = Materials.air;
		block = Materials.air;
		tiledata = null;
		blockdata = null;
	}
}
