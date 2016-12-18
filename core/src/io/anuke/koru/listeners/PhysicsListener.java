package io.anuke.koru.listeners;

import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.components.ProjectileComponent;
import io.anuke.koru.components.VelocityComponent;
import io.anuke.koru.entities.KoruEntity;

public class PhysicsListener extends CollisionListener{
	private final Vector2 tmp = new Vector2();

	@Override
	boolean accept(KoruEntity entity, KoruEntity other){
		return entity.hasComponent(VelocityComponent.class) && other.hasComponent(VelocityComponent.class) && !entity.hasComponent(ProjectileComponent.class);
	}

	@Override
	void collision(KoruEntity a, KoruEntity b){
		tmp.set(a.getX()-b.getX(), a.getY()-b.getY()).nor();
		
		Vector2 velocitya = a.getComponent(VelocityComponent.class).velocity;
		Vector2 velocityb = b.getComponent(VelocityComponent.class).velocity;
		
		velocitya.add(tmp.scl(1f));
		velocityb.add(tmp.scl(-1f));
	}
}
