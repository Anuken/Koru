package io.anuke.koru.items;

import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.world.Tile;

public interface Item{
	
	public default void clickEvent(InventoryComponent inventory, ItemStack stack, int x, int y, Tile clicked){}
	
	public default int getMaxStackSize(){
		return 40;
	}
	
	public String name();
}
