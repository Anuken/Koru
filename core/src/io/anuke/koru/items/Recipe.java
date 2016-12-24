package io.anuke.koru.items;

import io.anuke.koru.world.Material;

//TODO implements customizable recipes (extendable recipes, that is)
public interface Recipe{
	public ItemStack[] requirements();
	public Material result();
}
