package io.anuke.koru.components;

import io.anuke.aabb.Collider;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.systems.CollisionSystem;
import io.anuke.koru.systems.KoruEngine;

@Synced
public class ColliderComponent implements KoruComponent{
	public boolean collideterrain = false;
	public Collider collider = new Collider();
	public transient float terrainScl = 0.7f; //how much shorter the hitbox becomes when colliding with terrain
	public transient boolean grounded = true;
	public transient float lastx, lasty; //last collider x/y
	public transient boolean init = false;
	
	@Override
	public void onRemove(KoruEntity entity){
		KoruEngine.instance().getSystem(CollisionSystem.class).getColliderEngine().removeCollider(collider);
	}
}
