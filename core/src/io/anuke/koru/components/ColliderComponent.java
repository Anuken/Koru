package io.anuke.koru.components;

import io.anuke.aabb.Collider;
import io.anuke.koru.utils.Hitbox;

public class ColliderComponent implements KoruComponent{
	public boolean collideterrain = false;
	public Collider collider = new Collider();
	public Hitbox terrain = new Hitbox();
}
