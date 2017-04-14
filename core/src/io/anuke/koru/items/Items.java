package io.anuke.koru.items;

import static io.anuke.koru.items.ItemType.material;

public class Items{
	
	public static final Item
	
	stick = new Item("stick", material),
	
	wood = new Item("wood", material),
	
	pinecone = new Item("pinecone", material),
	
	stone = new Item("stone", material),
	
	mushroom = new Item("mushroom", material),
	
	tool = new Item("tool", ItemType.tool){
		
	},
	
	hammer = new Item("Hammer", ItemType.tool, ItemType.placer){
		
	};
}
