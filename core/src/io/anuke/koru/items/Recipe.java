package io.anuke.koru.items;

import io.anuke.koru.world.materials.IMaterial;

//TODO implements customizable recipes (extendable recipes, that is)
public interface Recipe{
	public ItemStack[] requirements();
	public IMaterial result();
}
