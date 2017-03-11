package io.anuke.koru.items;

import io.anuke.koru.world.materials.IMaterial;
import io.anuke.koru.world.materials.StructMaterial;

public enum Recipes implements Recipe{
	woodblock(StructMaterial.woodblock, new ItemStack(Items.wood, 2)),
	stonepillar(StructMaterial.stonepillar, new ItemStack(Items.stone, 2)),
	torch(StructMaterial.torch, new ItemStack(Items.wood, 1)),
	workbench(StructMaterial.workbench, new ItemStack(Items.wood, 10));
	
	IMaterial result;
	ItemStack[] requirements;
	
	private Recipes(IMaterial result, ItemStack... requirements){
		this.result = result;
		this.requirements = requirements;
	}

	public ItemStack[] requirements(){
		return requirements;
	}

	public IMaterial result(){
		return result;
	}

}
