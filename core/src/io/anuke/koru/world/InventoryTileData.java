package io.anuke.koru.world;

import io.anuke.koru.components.InventoryComponent;

public class InventoryTileData extends TileData{
	public final InventoryComponent inventory;
	
	@SuppressWarnings("unused")
	private InventoryTileData(){inventory=null;}
	
	public InventoryTileData(int width, int height){
		inventory = new InventoryComponent(width, height);
	}
}
