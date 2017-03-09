package io.anuke.koru.components;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.network.syncing.SyncData.Synced;

@Synced
public class TextComponent implements KoruComponent{
	public Color color = Color.WHITE;
	public String text;
}
