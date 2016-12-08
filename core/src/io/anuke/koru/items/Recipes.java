package io.anuke.koru.items;

import io.anuke.koru.world.Material;
import io.anuke.koru.world.Materials;

public enum Recipes implements Recipe{
	woodblock(Materials.woodblock, new ItemStack(Items.wood, 2)),
	woodblock1(Materials.stoneblock, new ItemStack(Items.stone, 2)),
	woodblock2(Materials.torch, new ItemStack(Items.wood, 1)),
	woodblock3(Materials.woodfloor, new ItemStack(Items.wood, 1)),
	woodblock4(Materials.woodblock, new ItemStack(Items.wood, 1)),
	woodblock5(Materials.woodblock, new ItemStack(Items.wood, 1)),
	woodblock6(Materials.woodblock, new ItemStack(Items.wood, 1)),
	woodblock7(Materials.woodblock, new ItemStack(Items.wood, 1)),
	woodblock8(Materials.woodblock, new ItemStack(Items.wood, 1)),
	woodblock9(Materials.woodblock, new ItemStack(Items.wood, 1));
	
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
