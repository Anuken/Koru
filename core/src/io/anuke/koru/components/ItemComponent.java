package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.SyncBuffer.Synced;

@Synced
public class ItemComponent implements Component{
	public ItemStack stack;
}
