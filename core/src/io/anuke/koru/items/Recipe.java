package io.anuke.koru.items;

import io.anuke.koru.world.materials.BaseMaterial;

//TODO implements customizable recipes (extendable recipes, that is)
public interface Recipe{
	public ItemStack[] requirements();
	public BaseMaterial result();
}
