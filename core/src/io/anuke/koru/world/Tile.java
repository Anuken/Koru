package io.anuke.koru.world;

import java.util.Arrays;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.koru.modules.World;
import io.anuke.koru.network.IServer;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialLayer;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.util.Bits;

public class Tile implements Poolable{
	public static final int LAYER_SIZE = 10;
	
	/**block light level; used for caves*/
	public byte light = (byte)127;
	/**top floor id*/
	public byte top = 0;
	/**floor ID array*/
	public short[] floors;
	/**wall block ID*/
	public short wallid;
	/**Tile data. Currently not saved or transferred.*/
	public transient TileData data;
	/**Position data. Transient, so it doesn't get sent over the network or serialized.
	 * MAKE SURE TO INITIALIZE AFTER DESERALIZATION! */
	public transient int x, y;
	
	/**Used for deserialization.*/
	public static Tile unloadedTile(){
		return new Tile();
	}
	
	private Tile(){}
	
	public Tile(Material floor, Material block){
		floors = new short[LAYER_SIZE];
		floors[top] = (short)floor.id();
		wallid = (short)block.id();
	}
	
	public float worldx(){
		return x * World.tilesize;
	}
	
	public float worldy(){
		return y * World.tilesize;
	}
	
	public float light(){
		return light/127f;
	}
	
	public void setLight(float f){
		light = (byte)(f*127);
	}
	
	public short tileid(){
		return floors[top];
	}
	
	public Material topFloor(){
		return !IServer.active() ? Material.getMaterial(floors[0]) : Material.getMaterial(floors[top]);
	}
	
	public Material wall(){
		return Material.getMaterial(wallid);
	}
	
	public boolean isWallEmpty(){
		return wallid == 0;
	}

	public void setWall(Material m){
		wallid = (short)m.id();
	}
	
	public void addFloor(Material m){
		if(!canAddTile()) throw new RuntimeException("Too many tiles added!");
		
		floors[++top] = (short)m.id();
	}
	
	public void removeTile(){
		if(!canRemoveTile()) throw new RuntimeException("Too many tiles removed!");
		
		floors[top--] = 0;
	}
	
	public boolean canAddTile(){
		return top+1 < LAYER_SIZE;
	}
	
	public boolean canRemoveTile(){
		return top > 0;
	}
	
	/**Sets either the top block or tile. Used mostly for generation.*/
	public void setMaterial(Material m){
		if(m == Materials.air){
			wallid = (short)Materials.air.id();
		}else if(m.layer() == MaterialLayer.floor){
			floors[top] = (short)m.id();
		}else{
			wallid = (short)m.id();
		}
	}
	
	/**Adds a material on top instead of setting it.*/
	public void addMaterial(Material m){
		if(m == Materials.air){
			wallid = (short)Materials.air.id();
		}else if(m.layer() == MaterialLayer.floor){
			addFloor(m);
		}else{
			wallid = (short)m.id();
		}
	}

	public boolean solid(){
		return topFloor().solid() || wall().solid();
	}

	public Material solidMaterial(){
		if(wall().solid()) return wall();
		if(topFloor().solid()) return topFloor();
		return null;
	}
	
	/**Random number, generated based on coordinates.*/
	public int rand(int max){
		return rand(0, max);
	}
	
	/**Random number, generated based on coordinates.*/
	public int rand(int offset, int max){
		MathUtils.random.setSeed(getSeed() + offset);
	    return MathUtils.random(1, max);
	}

	public void changeEvent(){
		topFloor().changeEvent(this);
		wall().changeEvent(this);
	}
	
	public long getSeed(){
		return Bits.packLong(x, y);
	}

	@Override
	public String toString(){
		return "Tile: [" + x + ", " + y + ", block=" + wall() + " tile=" + topFloor() + " {"+  light +"}]";
	}

	@Override
	public void reset(){
		Arrays.fill(floors, (short)0);
	}
}
