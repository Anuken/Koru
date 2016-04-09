package net.pixelstatic.koru.components;

import net.pixelstatic.koru.items.ItemStack;

import com.badlogic.ashley.core.Component;

public class InventoryComponent implements Component{
	public ItemStack[][] inventory;
	
	public InventoryComponent(int width, int height){
		inventory = new ItemStack[width][height];
	}
		
}
