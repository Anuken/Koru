package io.anuke.koru.systems;


import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.koru.components.DestroyOnTerrainHitComponent;
import io.anuke.koru.components.HitboxComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.listeners.CollisionHandler;
import io.anuke.koru.modules.World;
import io.anuke.koru.network.IServer;
import io.anuke.koru.world.Tile;

public class CollisionSystem extends KoruSystem{
	private final static float maxHitboxSize = 40;
	private final static float stepsize = 0.1f;
	private CollisionHandler handler;
	private ObjectSet<Long> iterated = new ObjectSet<Long>();
	private long lastFrameID;
	private Rectangle rect = new Rectangle();
	private GridPoint2 point = new GridPoint2();
	private Vector2 vector = new Vector2();

	public CollisionSystem(){
		super(Family.all(HitboxComponent.class, PositionComponent.class).get());
		handler = new CollisionHandler();
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		checkFrame();

		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		hitbox.entityhitbox.update(entity);

		checkCollisions(entity, hitbox);
		
		GridPoint2 point = vectorTerrainCollisions(entity, 0, 0);
		if(point != null){ //AHH ENTITY STUCK IN BLOCK!!
			float blockx = World.world(point.x), blocky = World.world(point.y);
			vector.set(entity.getX() - blockx, entity.getY() - blocky).setLength(1f);
			entity.position().add(vector.x, vector.y);
		}
	}

	void blockCollisionEvent(KoruEntity entity){
		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		if(hitbox.collideterrain && entity.mapComponent(DestroyOnTerrainHitComponent.class) != null){
			entity.removeSelfServer();
		}
	}

	void checkCollisions(KoruEntity entity, HitboxComponent hitbox){
		
		if(hitbox.sleeping) return;
		
		getEngine().map().getNearbyEntities(entity.getX(), entity.getY(), maxHitboxSize, 
		(aentity)-> { return aentity.hasComponent(HitboxComponent.class); }, 
		(other) -> {
			if(other == entity || iterated.contains(other.getID())) return;
			
			HitboxComponent otherhitbox = other.mapComponent(HitboxComponent.class);
			otherhitbox.entityhitbox.update(other);
			if(hitbox.entityhitbox.collides(otherhitbox.entityhitbox) && otherhitbox.entityhitbox.collides(hitbox.entityhitbox) 
					&& entity.getType().collide(entity, other) && other.getType().collide(other, entity)){
				collisionEvent(entity,other);
				otherhitbox.sleeptime = HitboxComponent.sleepduration;
			}	
		});
		
		iterated.add(entity.getID());
	}

	void collisionEvent(KoruEntity entitya, KoruEntity entityb){
		handler.dispatchEvent(entitya, entityb);
	}


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
			
			if(checkTerrainCollisions(entity, 0, ay) || i == steps-1){
				ay -= stepy;
				break;
			}
		}
		
		entity.position().add(ax, ay);
	}

	public GridPoint2 vectorTerrainCollisions(KoruEntity entity, float mx, float my){
		
		World world = World.instance();
		
		HitboxComponent component = entity.mapComponent(HitboxComponent.class);
		float newx = entity.getX() + mx;
		float newy = entity.getY() + my;
		component.terrainhitbox.update(entity, mx, my);
		int tilex = World.tile(newx);
		int tiley = World.tile(newy);
		for(int rx = -1;rx <= 1;rx ++){
			for(int ry = -1;ry <= 1;ry ++){
				int worldx = tilex + rx, worldy = tiley + ry;
				if(!world.inBounds(worldx, worldy)) continue;
				Tile tile = world.tile(worldx, worldy);
				if( !tile.solid()) continue;
				if(component.terrainhitbox.collides(tile.solidMaterial().getType().getRect(worldx, worldy, rect))){
					return point.set(worldx, worldy);
				}
				//	rect.set(0,0,0,0);
			}
		}
		return null;
	}
	
	public boolean checkTerrainCollisions(KoruEntity entity, float mx, float my){
		return vectorTerrainCollisions(entity, mx, my) != null;
	}

	private void checkFrame(){
		if(IServer.instance().getFrameID() != lastFrameID){
			iterated.clear();
			lastFrameID = IServer.instance().getFrameID();
		}
	}

}
