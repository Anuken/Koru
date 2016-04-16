package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.HitboxComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.components.VelocityComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.server.KoruServer;
import net.pixelstatic.koru.server.KoruUpdater;

import com.badlogic.ashley.core.Family;

public class VelocitySystem extends KoruSystem{
	private CollisionSystem collisions;

	@SuppressWarnings("unchecked")
	public VelocitySystem(){
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}

	@Override
	protected void processEntity(KoruEntity entity, float delta){
		if(collisions == null) collisions = this.getEngine().getSystem(CollisionSystem.class);
		VelocityComponent velocity = entity.mapComponent(VelocityComponent.class);

		float addx = velocity.velocity.x * delta, addy = velocity.velocity.y * delta;

		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);

		if(hitbox != null && KoruServer.active && hitbox.collideterrain){
			collisions.moveWithCollisions(KoruUpdater.instance.world, entity, addx, addy);
		}else{
			entity.position().x += addx;
			entity.position().y += addy;
		}

		velocity.velocity.scl((float)Math.pow(1f - velocity.drag, delta));
	}

	public void updateVelocity(PositionComponent position, VelocityComponent velocity){

	}

}
