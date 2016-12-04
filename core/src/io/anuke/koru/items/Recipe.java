package io.anuke.koru.items;

import io.anuke.koru.world.Material;

public interface Recipe{
	public ItemStack[] requirements();
	public Material result();
}
