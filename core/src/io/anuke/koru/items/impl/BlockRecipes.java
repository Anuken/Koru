package io.anuke.koru.items.impl;

import static io.anuke.koru.items.BlockRecipe.addRecipe;

import io.anuke.koru.world.materials.StructMaterials;

public class BlockRecipes{
	
	public static void load(){
		addRecipe(StructMaterials.woodblock);
		addRecipe(StructMaterials.stonepillar);
		addRecipe(StructMaterials.woodfloor);
		addRecipe(StructMaterials.stonefloor);
		addRecipe(StructMaterials.torch);
	}
}
