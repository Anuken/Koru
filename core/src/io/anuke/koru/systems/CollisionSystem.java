package io.anuke.koru.systems;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.anuke.aabb.Collider;
import io.anuke.aabb.ColliderEngine;
import io.anuke.koru.Koru;
import io.anuke.koru.components.ColliderComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.listeners.CollisionHandler;
import io.anuke.koru.modules.World;
import io.anuke.koru.network.IServer;
import io.anuke.koru.world.Tile;

public class CollisionSystem extends KoruSystem{
	private final static float maxHitboxSize = 50;
	private final static float stepsize = 0.1f;
	private ColliderEngine engine;
	private GridPoint2 point = new GridPoint2();
	private Vector2 vector = new Vector2();
	private CollisionHandler handler = new CollisionHandler();

	public CollisionSystem() {
		super(Family.all(ColliderComponent.class, PositionComponent.class).get());
		engine = new ColliderEngine();
		
		//setup contact listeners/filters on server
		if(IServer.active()){
			engine.setContactFilter((a, b) -> {
				KoruEntity ka = (KoruEntity) a.data;
				KoruEntity kb = (KoruEntity) b.data;
				return ka.getType().collide(ka, kb) && kb.getType().collide(kb, ka);
			});

			engine.setContactListener((a, b) -> {
				KoruEntity ka = (KoruEntity) a.data;
				KoruEntity kb = (KoruEntity) b.data;
				handler.dispatchEvent(ka, kb);
			});
		}
		
		//prevents objects from moving into walls
		engine.setPositionVerifier((c, x, y)->{
			Rectangle col = c.getBounds();
			col.x = x - c.w / 2;
			col.y = y - c.h / 2;
			
			return !checkCollisions(x, y, col);
		});
		
		
	}

	public ColliderEngine getColliderEngine(){
		return engine;
	}

	@Override
	public void update(float deltaTime){
		engine.updateCollisions();
		super.update(deltaTime);
		engine.updateForces(deltaTime / 60f);
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		ColliderComponent co = entity.collider();

		if(!co.init){
			co.collider.x = entity.getX();
			co.collider.y = entity.getY() + (co.grounded ? co.collider.h / 2 : 0);
			co.lastx = co.collider.x;
			co.lasty = co.collider.y;
			co.collider.data = entity;
			engine.addCollider(co.collider);
			co.init = true;
		}else{
			entity.position().add(co.collider.x - co.lastx, co.collider.y - co.lasty);
			co.collider.setPosition(entity.getX(), entity.getY() + (co.grounded ? co.collider.h / 2 : 0));
			co.lastx = co.collider.x;
			co.lasty = co.collider.y;

		}

		if(!co.collider.trigger)
			checkTerrainCollisions(co.collider, co);
	}

	void checkTerrainCollisions(Collider collider, ColliderComponent comp){
		float x = collider.x + collider.getVelocity().x * Koru.delta();
		float y = collider.y + collider.getVelocity().y * Koru.delta();
		float w = collider.w;
		float h = collider.h;
		
		checkTerrain(x, y, out->{
			Rectangle col = collider.getBounds();

			col.height *= comp.terrainScl;

			col.x = x - w / 2;

			if(col.overlaps(out)){
				collider.getVelocity().scl(-1f * collider.restitution, 1f);
			}

			col.x = collider.x - w / 2;

			col.y = y - h / 2;

			if(col.overlaps(out)){
				collider.getVelocity().scl(1f, -1f * collider.restitution);
			}
		});
		
		Rectangle col = collider.getBounds();
		col.height *= comp.terrainScl;
		
		//prevent collider from getting stuck in a block.
		checkTerrain(x, y, out->{
			if(col.overlaps(out)){
				out.getCenter(vector);
				vector.sub(collider.x, collider.y);
				vector.setLength(1f);
				
				collider.x -= vector.x;
				collider.y -= vector.y;
			}
		});
	}
	
	void checkTerrain(float x, float y, Consumer<Rectangle> cons){
		World world = World.instance();
		int tilex = World.tile(x);
		int tiley = World.tile(y);
		int range = 1;
		for(int rx = -range; rx <= range; rx++){
			for(int ry = -range; ry <= range; ry++){
				int worldx = tilex + rx, worldy = tiley + ry;
				if(!world.inBounds(worldx, worldy))
					continue;
				Tile tile = world.tile(worldx, worldy);
				if(!tile.solid())
					continue;

				Rectangle out = tile.solidMaterial().getType().getHitbox(worldx, worldy, Rectangle.tmp2);
				cons.accept(out);
			}
		}
	}
	
	boolean checkCollisions(float x, float y, Rectangle rect){
		World world = World.instance();
		int tilex = World.tile(x);
		int tiley = World.tile(y);
		int range = 1;
		for(int rx = -range; rx <= range; rx++){
			for(int ry = -range; ry <= range; ry++){
				int worldx = tilex + rx, worldy = tiley + ry;
				if(!world.inBounds(worldx, worldy))
					continue;
				Tile tile = world.tile(worldx, worldy);
				if(!tile.solid())
					continue;

				Rectangle out = tile.solidMaterial().getType().getHitbox(worldx, worldy, Rectangle.tmp2);
				
				if(out.overlaps(rect)) return true;
			}
		}
		
		return false;
	}
}
