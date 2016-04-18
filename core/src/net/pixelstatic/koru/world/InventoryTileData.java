package net.pixelstatic.koru.world;

import net.pixelstatic.koru.components.InventoryComponent;

public class InventoryTileData extends TileData{
	public final InventoryComponent inventory;
	
	public InventoryTileData(int width, int height){
		inventory = new InventoryComponent(width, height);
	}
}
