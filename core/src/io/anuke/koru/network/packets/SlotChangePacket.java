package io.anuke.koru.network.packets;

import io.anuke.koru.items.ItemStack;

public class SlotChangePacket{
	public int slot;
	public int id;
	public ItemStack stack;
}
