package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.DestroyOnTerrainHitComponent;
import net.pixelstatic.koru.components.HitboxComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.listeners.CollisionHandler;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Tile;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectSet;

public class CollisionSystem extends KoruSystem{
	private CollisionHandler handler;
	ObjectSet<Long> iterated = new ObjectSet<Long>();
	long lastFrameID;
	private Rectangle rect = new Rectangle();

	@SuppressWarnings("unchecked")
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
	}

	void blockCollisionEvent(KoruEntity entity){
		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		if(hitbox.collideterrain && entity.mapComponent(DestroyOnTerrainHitComponent.class) != null){
			entity.removeSelfServer();
		}
	}

	void checkCollisions(KoruEntity entity, HitboxComponent hitbox){
		ImmutableArray<Entity> otherEntities = getEngine().getEntitiesFor(this.getFamily());
		for(Entity otherentity : otherEntities){
			KoruEntity other = (KoruEntity)otherentity;
			if(other == entity || iterated.contains(other.getID())) continue;
			HitboxComponent otherhitbox = other.mapComponent(HitboxComponent.class);
			otherhitbox.entityhitbox.update(other);
			if(hitbox.entityhitbox.collides(otherhitbox.entityhitbox) && otherhitbox.entityhitbox.collides(hitbox.entityhitbox) 
					&& entity.getType().collide(entity, other) && other.getType().collide(other, entity)){
				collisionEvent(entity,other);
			//	iterated.add(other.getID());
			}
		
		}
		iterated.add(entity.getID());
	}
	
	void collisionEvent(KoruEntity entitya, KoruEntity entityb){
		handler.dispatchEvent(entitya, entityb);
	}


	public void moveWithCollisions(World world, KoruEntity entity, float mx, float my){
		boolean wallcollision = false;
		if( !checkTerrainCollisions(world, entity, mx, 0)){
			entity.position().add(mx, 0);
		}else{
			blockCollisionEvent(entity);
			wallcollision = true;
		}

		if( !checkTerrainCollisions(world, entity, 0, my)){
			entity.position().add(0, my);
		}else{
			if( !wallcollision) blockCollisionEvent(entity);
		}
	}

	public boolean checkTerrainCollisions(World world, KoruEntity entity, float mx, float my){
		HitboxComponent component = entity.mapComponent(HitboxComponent.class);
		float newx = entity.getX() + mx;
		float newy = entity.getY() + my;
		component.terrainhitbox.update(entity, mx, my);
		int tilex = World.tile(newx);
		int tiley = World.tile(newy);
		for(int rx = -1;rx <= 1;rx ++){
			for(int ry = -1;ry <= 1;ry ++){
				int worldx = tilex + rx, worldy = tiley + ry;
				if( !World.inBounds(worldx, worldy)) continue;
				Tile tile = world.tiles[worldx][worldy];
				if( !tile.solid() || !tile.solidMaterial().collisionsEnabled()) continue;
				if(component.terrainhitbox.collides(tile.solidMaterial().getType().getRect(worldx, worldy, rect))){
					return true;
				}
				//	rect.set(0,0,0,0);
			}
		}
		return false;
	}

	private void checkFrame(){
		if(KoruUpdater.frameID() != lastFrameID){
			iterated.clear();
			lastFrameID = KoruUpdater.frameID();
		}
	}

}
