package io.anuke.koru.items.impl;

import static io.anuke.koru.items.ItemType.material;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.world.BreakType;

public class Items{
	
	public static final Item
	
	stick = new Item("stick"),
	
	wood = new Item("wood", material),
	
	stone = new Item("stone", material),
	
	pinecone = new Item("pinecone"),
	
	mushroom = new Item("mushroom"),
	
	axe = new Item("woodaxe", ItemType.tool){
		{
			formalName = "Wooden Axe";
			breakSpeeds.put(BreakType.wood, 1);
			breakSpeeds.put(BreakType.stone, 1);
		}
	},
	pickaxe = new Item("woodpick", ItemType.tool){
		{
			formalName = "Wooden Pickaxe";
			breakSpeeds.put(BreakType.stone, 1);
		}
	},
	
	hammer = new Item("hammer", ItemType.tool, ItemType.placer){
		
	},
	mushroomStaff = new Item("mushroomstaff", ItemType.tool){
		{
			formalName = "Mushroom Staff";
		}
	};
}
