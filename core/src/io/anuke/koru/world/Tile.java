package io.anuke.koru.world;

import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.koru.generation.MaterialManager;

public class Tile implements Poolable{
	public int tileid, blockid;
	public byte light = (byte)127;
	
	public Tile(){}
	
	public Tile(Material tile, Material block){
		tileid = tile.id();
		blockid = block.id();
	}
	
	public float light(){
		return light/127f;
	}
	
	public void setLight(float f){
		light = (byte)(f*127);
	}
	
	public Material tile(){
		return MaterialManager.instance().getMaterial(tileid);
	}
	
	public Material block(){
		return MaterialManager.instance().getMaterial(blockid);
	}
	
	public boolean tileEmpty(){
		return tileid == 0;
	}
	
	public boolean blockEmpty(){
		return blockid == 0;
	}

	public Tile setTileMaterial(Material m){
		tileid = m.id();
		return this;
	}

	public Tile setBlockMaterial(Material m){
		blockid = m.id();
		return this;
	}

	public void setMaterial(Material m){
		if(m == Materials.air){
			blockid = 0;
			return;
		}
		if(m.getType().tile()){
			tileid = m.id();
		}else{
			blockid = m.id();
		}
	}

	public boolean solid(){
		return tile().getType().solid() || block().getType().solid();
	}

	public Material solidMaterial(){
		if(block().getType().solid()) return block();
		if(tile().getType().solid()) return tile();
		return null;
	}

	public void changeEvent(){
		tile().changeEvent(this);
		block().changeEvent(this);
	}

	public String toString(){
		return "Tile:[block=" + tile() + " tile=" + block() + " {"+  light +"} ]";
	}

	@Override
	public void reset(){
		tileid = 0;
		blockid = 0;
	}
}
