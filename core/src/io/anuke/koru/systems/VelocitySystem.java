package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.VelocityComponent;
import io.anuke.koru.entities.KoruEntity;

public class VelocitySystem extends KoruSystem{
	private CollisionSystem collisions;

	public VelocitySystem(){
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}

	@Override
	protected void processEntity(KoruEntity entity, float delta){
		if(collisions == null) collisions = this.getEngine().getSystem(CollisionSystem.class);
		VelocityComponent velocity = entity.get(VelocityComponent.class);
		velocity.velocity.limit(velocity.limit);
		
		float addx = velocity.velocity.x * delta, addy = velocity.velocity.y * delta;

		//ColliderComponent hitbox = entity.collider();

		//if(hitbox != null && IServer.active() && hitbox.collideterrain){
			//collisions.moveWithCollisions(entity, addx, addy);
		//}else{
			entity.pos().x += addx;
			entity.pos().y += addy;
		//}
		//Koru.log(velocity.velocity);
		velocity.velocity.scl((float)Math.pow(1f - velocity.drag, delta));
	}
}
