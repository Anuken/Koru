package io.anuke.koru.items;

import static io.anuke.koru.items.BlockRecipe.recipe;

import io.anuke.koru.world.materials.StructMaterials;

public class BlockRecipes{
	
	static{
		recipe(StructMaterials.woodblock);
		recipe(StructMaterials.stonepillar);
		recipe(StructMaterials.woodfloor);
		recipe(StructMaterials.stonefloor);
		recipe(StructMaterials.torch);
	}

	public static void load(){}
}
