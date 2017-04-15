package io.anuke.koru.items;

import static io.anuke.koru.items.ItemType.material;

public class Items{
	
	public static final Item
	
	stick = new Item("stick", ItemType.rod),
	
	wood = new Item("wood", material),
	
	stone = new Item("stone", material),
	
	pinecone = new Item("pinecone"),
	
	mushroom = new Item("mushroom"),
	
	tool = new Item("tool", ItemType.tool){
		
	},
	
	hammer = new Item("Hammer", ItemType.tool, ItemType.placer){
		
	};
}
