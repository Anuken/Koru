package io.anuke.koru.systems;


import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.anuke.aabb.ColliderEngine;
import io.anuke.koru.components.ColliderComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.entities.KoruEntity;

public class CollisionSystem extends KoruSystem{
	private final static float maxHitboxSize = 50;
	private final static float stepsize = 0.1f;
	private ColliderEngine engine;
	private GridPoint2 point = new GridPoint2();
	private Vector2 vector = new Vector2();

	public CollisionSystem(){
		super(Family.all(ColliderComponent.class, PositionComponent.class).get());
		engine = new ColliderEngine();
	}
	
	public ColliderEngine getColliderEngine(){
		return engine;
	}
	
	@Override
	public void update (float deltaTime) {
		engine.update(deltaTime/60f);
		super.update(deltaTime);
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		ColliderComponent co = entity.collider();
		
		if(!co.init){
			co.collider.x = entity.getX();
			co.collider.y = entity.getY();
			co.lastx = entity.getX();
			co.lasty = entity.getY();
			//co.collider.maxVelocity = 0.8f;
			//
			engine.addCollider(co.collider);
			co.init = true;
		}else{
			entity.position().add(co.collider.x - co.lastx, co.collider.y - co.lasty);
			co.lastx = co.collider.x;
			co.lasty = co.collider.y;
		}
			
		/*
		GridPoint2 point = getTerrainCollisions(entity, 0, 0);
		if(point != null){ //entity is stuck in block
			float blockx = World.world(point.x), blocky = World.world(point.y);
			vector.set(entity.getX() - blockx, entity.getY() - blocky).setLength(1f);
			entity.position().add(vector.x, vector.y);
		}
		*/
	}
	
	/*
	void blockCollisionEvent(KoruEntity entity){
		ColliderComponent hitbox = entity.collider();
		if(hitbox.collideterrain && entity.get(DestroyOnTerrainHitComponent.class) != null){
			entity.removeServer();
		}
	}

	//TODO make this use small steps instead of one jump
	public void moveWithCollisions(KoruEntity entity, float mx, float my){
		boolean wallcollision = false;
		if( !checkTerrainCollisions(entity, mx, 0)){
			entity.position().add(mx, 0);
		}else{
			blockCollisionEvent(entity);
			wallcollision = true;
		}

		if( !checkTerrainCollisions(entity, 0, my)){
			entity.position().add(0, my);
		}else{
			if( !wallcollision) blockCollisionEvent(entity);
		}
	}
	
	public void moveEntity(KoruEntity entity, float mx, float my){
		float stepsize = CollisionSystem.stepsize*Koru.delta();
		float stepx, stepy;
		int steps;
		
		if(Math.abs(mx) > Math.abs(my)){
			steps = (int)(Math.abs(mx)/stepsize);
			stepx = stepsize * (mx < 0 ? -1 : 1);
			stepy = my/steps;
		}else{
			steps = (int)(Math.abs(my)/stepsize);
			stepy = stepsize * (my < 0 ? -1 : 1);
			stepx = mx/steps;
		}
		
		float ax = 0;
		for(int i = 0; i < steps; i ++){
			ax += stepx;
			
			if(checkTerrainCollisions(entity, ax, 0) || i == steps-1){
				ax -= stepx;
				break;
			}
		}
		
		float ay = 0;
		for(int i = 0; i < steps; i ++){
			ay += stepy;
			
			if(checkTerrainCollisions(entity, ax, ay) || i == steps-1){
				ay -= stepy;
				break;
			}
		}
		
		entity.position().add(ax, ay);
	}
	
	/**Returns the point of terrain collision, or null if none are found
	public GridPoint2 getTerrainCollisions(KoruEntity entity, float mx, float my){
		
		World world = World.instance();
		
		ColliderComponent component = entity.collider();
		float newx = entity.getX() + mx;
		float newy = entity.getY() + my;
		component.terrain.update(entity, mx, my);
		int tilex = World.tile(newx);
		int tiley = World.tile(newy);
		for(int rx = -1;rx <= 1;rx ++){
			for(int ry = -1;ry <= 1;ry ++){
				int worldx = tilex + rx, worldy = tiley + ry;
				if(!world.inBounds(worldx, worldy)) continue;
				Tile tile = world.tile(worldx, worldy);
				if( !tile.solid()) continue;
				if(component.terrain.collides(tile.solidMaterial().getType().getRect(worldx, worldy, Rectangle.tmp))){
					return point.set(worldx, worldy);
				}
			}
		}
		return null;
	}
	
	public boolean checkTerrainCollisions(KoruEntity entity, float mx, float my){
		return getTerrainCollisions(entity, mx, my) != null;
	}
	*/
}
