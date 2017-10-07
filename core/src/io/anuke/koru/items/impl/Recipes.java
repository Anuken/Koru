package io.anuke.koru.items.impl;

import static io.anuke.koru.items.Recipe.addRecipe;

import io.anuke.koru.items.ItemStack;

public class Recipes{
	
	public static void load(){
		addRecipe(Items.hammer, new ItemStack(Items.wood, 5), new ItemStack(Items.stick, 5));
		addRecipe(Items.axe, new ItemStack(Items.stone, 5), new ItemStack(Items.stick, 5));
		addRecipe(Items.mushroomStaff, new ItemStack(Items.stick, 10), new ItemStack(Items.mushroom, 5));
	}
}
