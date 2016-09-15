package io.anuke.koru.components;

import io.anuke.koru.world.Material;

import com.badlogic.ashley.core.Component;

public class TileComponent implements Component{
	public int tilex, tiley;
	public Material material;
}
