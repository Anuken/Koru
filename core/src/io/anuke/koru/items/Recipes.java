package io.anuke.koru.items;

import io.anuke.koru.world.Material;
import io.anuke.koru.world.Materials;

public enum Recipes implements Recipe{
	woodblock(Materials.woodblock, new ItemStack(Items.wood, 2)),
	stonepillar(Materials.stonepillar, new ItemStack(Items.stone, 2)),
	torch(Materials.torch, new ItemStack(Items.wood, 1)),
	workbench(Materials.workbench, new ItemStack(Items.wood, 10));
	
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
