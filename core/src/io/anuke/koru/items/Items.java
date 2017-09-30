package io.anuke.koru.items;

import static io.anuke.koru.items.ItemType.material;

import io.anuke.koru.world.BreakType;

public class Items{
	
	public static final Item
	
	stick = new Item("stick", ItemType.rod),
	
	wood = new Item("wood", material),
	
	stone = new Item("stone", material),
	
	pinecone = new Item("pinecone"),
	
	mushroom = new Item("mushroom"),
	
	tool = new Item("tool", ItemType.tool){
		{
			
		}
		
		public float getBreakSpeed(BreakType type){
			return 1;
		}
	},
	
	hammer = new Item("hammer", ItemType.tool, ItemType.placer){
		
	};
}
