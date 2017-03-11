package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.items.ItemStack;
import io.anuke.koru.world.Tile;

public interface IMaterial{
	public Vector3 foilageTint();
	
	public default int breaktime(){return 0;} // 0 means not breakable
	
	public default boolean collisionsEnabled(){return true;}
	
	public int id();
	
	public String name();
	
	public IMaterialType getType();
	
	public default float offset(){return 0;}
	
	public default Color getColor(){return Color.WHITE;}
	
	public default ItemStack[] getDrops(){return null;}
	
	public default void changeEvent(Tile tile){}
	
	public default int variants(){return 1;}
	
	public default boolean breakable(){return breaktime() > 0;}
	
	/*
	public static boolean isPlaceable(Material material, Tile tile){
		
		if(!material.getType().tile()){
			return (tile.blockEmpty() || tile.block().getType() == IMaterialType.foilage);
		}else{
			return tile.tile() != material && 
				(tile.blockEmpty() || tile.block().getType() == IMaterialType.torch);
		}		
	}
	*/
}
