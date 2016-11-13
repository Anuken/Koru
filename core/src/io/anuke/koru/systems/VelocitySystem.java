package io.anuke.koru.systems;

import io.anuke.koru.components.HitboxComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.VelocityComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.IServer;
import io.anuke.koru.world.World;

import com.badlogic.ashley.core.Family;

public class VelocitySystem extends KoruSystem{
	private CollisionSystem collisions;

	public VelocitySystem(){
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}

	@Override
	protected void processEntity(KoruEntity entity, float delta){
		if(collisions == null) collisions = this.getEngine().getSystem(CollisionSystem.class);
		VelocityComponent velocity = entity.mapComponent(VelocityComponent.class);

		float addx = velocity.velocity.x * delta, addy = velocity.velocity.y * delta;

		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);

		if(hitbox != null && IServer.active() && hitbox.collideterrain){
			collisions.moveWithCollisions(World.instance(), entity, addx, addy);
		}else{
			entity.position().x += addx;
			entity.position().y += addy;
		}

		velocity.velocity.scl((float)Math.pow(1f - velocity.drag, delta));
		
		velocity.velocity.limit(velocity.limit);
	}

	public void updateVelocity(PositionComponent position, VelocityComponent velocity){

	}

}
