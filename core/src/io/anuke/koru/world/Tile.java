package io.anuke.koru.world;

import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.koru.world.materials.BaseMaterial;
import io.anuke.koru.world.materials.Material;

public class Tile implements Poolable{
	public int tileid, blockid;
	public byte light = (byte)127;
	
	public Tile(){}
	
	public Tile(BaseMaterial tile, BaseMaterial block){
		tileid = tile.id();
		blockid = block.id();
	}
	
	public float light(){
		return light/127f;
	}
	
	public void setLight(float f){
		light = (byte)(f*127);
	}
	
	public BaseMaterial tile(){
		return BaseMaterial.getMaterial(tileid);
	}
	
	public BaseMaterial block(){
		return BaseMaterial.getMaterial(blockid);
	}
	
	public boolean tileEmpty(){
		return tileid == 0;
	}
	
	public boolean blockEmpty(){
		return blockid == 0;
	}

	public Tile setTileMaterial(BaseMaterial m){
		tileid = m.id();
		return this;
	}

	public Tile setBlockMaterial(BaseMaterial m){
		blockid = m.id();
		return this;
	}

	public void setMaterial(BaseMaterial m){
		if(m == Material.air){
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

	public BaseMaterial solidMaterial(){
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
