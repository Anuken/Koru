package io.anuke.koru.world;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.items.ItemStack;

public interface Material{
	public Color foilageColor();
	
	public default int breaktime(){return 0;} // 0 means not breakable
	
	public default boolean collisionsEnabled(){return true;}
	
	public int id();
	
	public String name();
	
	public MaterialType getType();
	
	public default float offset(){return 0;}
	
	public default Color getColor(){return Color.WHITE;}
	
	public default ItemStack[] getDrops(){return null;}
	
	public default void changeEvent(Tile tile){}
	
	public default int variants(){return 1;}
	
	public default boolean breakable(){return breaktime() > 0;}
	
	public static boolean isPlaceable(Material material, Tile tile){
		
		if(!material.getType().tile()){
			return (tile.blockEmpty() || tile.block().getType() == MaterialType.foilage);
		}else{
			return tile.tile() != material && 
				(tile.blockEmpty() || tile.block().getType() == MaterialType.torch);
		}		
	}
}
