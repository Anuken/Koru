package io.anuke.koru.items;

import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;

public enum Items implements Item{
	stick, wood, pinecone, water, stone,
	woodaxe(ItemType.tool, 1f){
		public boolean breaks(Material mat){
			return mat.getType() == MaterialType.tree || mat.name().contains("wood") || mat.name().contains("torch");
		}
	},
	woodpickaxe(ItemType.tool, 100f){
		public boolean breaks(Material mat){
			return mat.name().contains("stone") || mat.name().contains("rock");
		}
	},
	woodhammer(ItemType.hammer, 1f){
		
	};
	float power;
	ItemType type = ItemType.material;
	
	private Items(){}
	
	private Items(ItemType type, float power){
		this.power = power;
		this.type = type;
	}
	
	private Items(int stacksize){
		this.stacksize = stacksize;
	}
	
	private int stacksize = 40;
	
	public float power(){
		return power;
	}
	
	public boolean breaks(Material mat){
		return true;
	}
	
	public int getMaxStackSize(){
		return stacksize;
	}
	
	public ItemType type(){
		return type;
	}
}
