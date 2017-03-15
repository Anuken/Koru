package io.anuke.koru.world;

import java.util.Arrays;

import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.koru.world.materials.BaseMaterial;

public class Tile implements Poolable{
	public static final int LAYER_SIZE = 10;
	
	public byte light = (byte)127, top = 0;
	public int[] layers;
	public int blockid;
	
	/**Used for deserialization.*/
	public static Tile unloadedTile(){
		return new Tile();
	}
	
	private Tile(){}
	
	public Tile(BaseMaterial tile, BaseMaterial block){
		layers = new int[LAYER_SIZE];
		layers[top] = tile.id();
		blockid = block.id();
	}
	
	public float light(){
		return light/127f;
	}
	
	public void setLight(float f){
		light = (byte)(f*127);
	}
	
	public int tileid(){
		return layers[top];
	}
	
	public BaseMaterial tile(){
		return BaseMaterial.getMaterial(layers[top]);
	}
	
	public BaseMaterial block(){
		return BaseMaterial.getMaterial(blockid);
	}
	
	public boolean blockEmpty(){
		return blockid == 0;
	}
	
	/*
	public void setTileMaterial(BaseMaterial m){
		tileid = m.id();
	}
	*/

	public void setBlockMaterial(BaseMaterial m){
		blockid = m.id();
	}

	public void setMaterial(BaseMaterial m){
		if(m.getType().tile()){
			layers[top] = m.id();
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
		return "Tile:[block=" + block() + " tile=" + tile() + " {"+  light +"} ]";
	}

	@Override
	public void reset(){
		Arrays.fill(layers, 0);
	}
}
