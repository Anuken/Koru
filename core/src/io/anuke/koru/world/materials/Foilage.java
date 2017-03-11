package io.anuke.koru.world.materials;

import static io.anuke.koru.world.MaterialType.tile;

import io.anuke.koru.world.MaterialType;

public class Foilage extends BaseMaterial{
	Foilage grass = new Foilage("grass", tile){{
		variants = 8;
	}};
	
	protected Foilage(String name, MaterialType type) {
		super(name, type);
	}
}
