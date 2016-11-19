package io.anuke.koru.network.packets;

import io.anuke.koru.items.ItemStack;

public class InventoryUpdatePacket{
	public ItemStack[][] stacks;
	public ItemStack selected;
}
