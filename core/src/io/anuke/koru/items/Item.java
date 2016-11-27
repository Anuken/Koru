package io.anuke.koru.items;

import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.world.Tile;

public enum Item{
	stick, wood, pinecone, water, stone,
	woodaxe{
		public void clickEvent(InventoryComponent inventory, ItemStack stack, int x, int y, Tile clicked){
			
		}
	};
	
	private Item(){
		
	}
	
	private Item(int stacksize){
		this.stacksize = stacksize;
	}
	
	public void clickEvent(InventoryComponent inventory, ItemStack stack, int x, int y, Tile clicked){
		
	}
	
	private int stacksize = 40;
	
	public int getMaxStackSize(){
		return stacksize;
	}
}
