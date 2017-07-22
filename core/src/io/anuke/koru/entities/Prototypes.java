package io.anuke.koru.entities;

import io.anuke.koru.entities.types.*;
import io.anuke.ucore.ecs.Prototype;

public class Prototypes{
	public static final Prototype
	
	projectile = new Projectile(),
	player = new Player(),
	testEntity = new TestEntity(),
	blockAnimation = new BlockAnimation(),
	indicator = new DamageIndicator(),
	particle = new Particle(),
	itemDrop = new ItemDrop(),
	effect = new Effect();
}
