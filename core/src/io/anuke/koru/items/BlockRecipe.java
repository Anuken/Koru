package io.anuke.koru.items;

import io.anuke.koru.world.materials.StructMaterial;

public class BlockRecipe{
	public static final BaseBlockRecipe woodpillar = new BaseBlockRecipe(StructMaterial.woodblock);
	public static final BaseBlockRecipe stonepillar = new BaseBlockRecipe(StructMaterial.stonepillar);
	public static final BaseBlockRecipe woodfloor = new BaseBlockRecipe(StructMaterial.woodfloor);
	public static final BaseBlockRecipe stonefloor = new BaseBlockRecipe(StructMaterial.stonefloor);
	public static final BaseBlockRecipe torch = new BaseBlockRecipe(StructMaterial.torch);
	public static final BaseBlockRecipe workbench = new BaseBlockRecipe(StructMaterial.workbench);

	public static void load(){}
}
