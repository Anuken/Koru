package io.anuke.koru.world;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.items.ItemStack;

public interface Material{
	public Color foilageColor();
	
	public default int breakTime(){return 0;} // 0 means not breakable
	
	public default boolean collisionsEnabled(){return true;}
	
	public int id();
	
	public String name();
	
	public MaterialType getType();
	
	public default float offset(){return 0;}
	
	public default TileData getDefaultData(){return null;}
	
	public default Class<? extends TileData> getDataClass(){return null;}
	
	public default Color getColor(){return Color.WHITE;}
	
	public default ItemStack[] getDrops(){return null;}
	
	public default void changeEvent(Tile tile){}
	
	public default int variants(){return 1;}
}
