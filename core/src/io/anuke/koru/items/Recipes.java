package io.anuke.koru.items;

import io.anuke.koru.world.Material;
import io.anuke.koru.world.Materials;

public enum Recipes implements Recipe{
	woodblock(Materials.woodblock, new ItemStack(Items.wood, 1));
	Material result;
	ItemStack[] requirements;
	
	private Recipes(Material result, ItemStack... requirements){
		this.result = result;
		this.requirements = requirements;
	}

	public ItemStack[] requirements(){
		return requirements;
	}

	public Material result(){
		return result;
	}

}
