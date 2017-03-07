package io.anuke.koru.components;

import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.SyncData.Synced;

@Synced
public class ItemComponent implements KoruComponent{
	public ItemStack stack;
}
