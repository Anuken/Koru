package io.anuke.koru.components;

import io.anuke.koru.network.SyncData.Synced;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

@Synced
public class TextComponent implements Component{
	public Color color = Color.WHITE;
	public String text;
}
