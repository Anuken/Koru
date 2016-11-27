package io.anuke.koru.items;

import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.network.IServer;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;

public enum Items implements Item{
	stick, wood, pinecone, water, stone,
	woodaxe{
		public void clickEvent(InventoryComponent inventory, ItemStack stack, int x, int y, Tile clicked){
			if(clicked.block().breakable()){
				clicked.setBlockMaterial(Materials.air);
				IServer.instance().getWorld().updateTile(x, y);
			}
		}
	};
	
	private Items(){
		
	}
	
	private Items(int stacksize){
		this.stacksize = stacksize;
	}
	
	public void clickEvent(InventoryComponent inventory, ItemStack stack, int x, int y, Tile clicked){
		
	}
	
	private int stacksize = 40;
	
	public int getMaxStackSize(){
		return stacksize;
	}
}
