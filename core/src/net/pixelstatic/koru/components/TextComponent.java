package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;

@Synced
public class TextComponent implements Component{
	public String text;
}
