package io.anuke.koru.items;

import io.anuke.koru.world.materials.StructMaterial;

public class BlockRecipe{
	public static final BaseBlockRecipe stonepillar = new BaseBlockRecipe(StructMaterial.stonepillar, new ItemStack(Items.stone, 2));
	public static final BaseBlockRecipe torch = new BaseBlockRecipe(StructMaterial.torch, new ItemStack(Items.wood, 1));
	public static final BaseBlockRecipe workbench = new BaseBlockRecipe(StructMaterial.workbench, new ItemStack(Items.wood, 10));
}
