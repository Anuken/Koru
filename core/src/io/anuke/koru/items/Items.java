package io.anuke.koru.items;

import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialTypes;

public enum Items implements Item{
	stick, wood, pinecone, water, stone, mushroom,
	woodaxe(ItemType.tool, 1f){
		public boolean breaks(Material mat){
			return mat.getType() == MaterialTypes.tree || mat.name().contains("wood") || mat.name().contains("torch");
		}
		
		public String formalName(){
			return "Wooden Axe";
		}
	},
	woodpickaxe(ItemType.tool, 1f){
		public boolean breaks(Material mat){
			return mat.name().contains("stone") || mat.name().contains("rock");
		}
	},
	woodhammer(ItemType.hammer, 1f){
		
	},
	woodsword(ItemType.weapon){
		public WeaponType weaponType(){
			return WeaponType.sword;
		}
	};
	float power;
	ItemType type = ItemType.material;
	
	private Items(){}
	
	private Items(ItemType type, float power){
		this.power = power;
		this.type = type;
	}
	
	private Items(ItemType type){
		this.type = type;
	}
	
	private Items(int stacksize){
		this.stacksize = stacksize;
	}
	
	private int stacksize = 100;
	
	public float power(){
		return power;
	}
	
	public boolean breaks(Material mat){
		return false;
	}
	
	public int getMaxStackSize(){
		return stacksize;
	}
	
	public ItemType type(){
		return type;
	}
}
