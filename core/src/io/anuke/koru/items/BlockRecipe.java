package io.anuke.koru.items;

import static io.anuke.koru.items.BaseBlockRecipe.recipe;

import io.anuke.koru.world.materials.StructMaterial;

public class BlockRecipe{
	
	static{
		recipe(StructMaterial.woodblock);
		recipe(StructMaterial.stonepillar);
		recipe(StructMaterial.woodfloor);
		recipe(StructMaterial.stonefloor);
		recipe(StructMaterial.torch);
		recipe(StructMaterial.workbench);
	}

	public static void load(){}
}
