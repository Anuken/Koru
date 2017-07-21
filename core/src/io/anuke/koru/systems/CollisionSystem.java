package io.anuke.koru.systems;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.listeners.CollisionHandler;
import io.anuke.koru.modules.World;
import io.anuke.koru.network.IServer;
import io.anuke.koru.traits.ColliderComponent;
import io.anuke.koru.traits.PositionComponent;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.util.Physics;

public class CollisionSystem extends KoruSystem{
	private final static float maxHitboxSize = 50;
	private final static float stepsize = 0.1f;
	private GridPoint2 point = new GridPoint2();
	private Vector2 vector = new Vector2();
	private CollisionHandler handler = new CollisionHandler();

	public CollisionSystem() {
		super(Family.all(ColliderComponent.class, PositionComponent.class).get());
	}

	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		ColliderComponent co = entity.collider();

		if(IServer.active()){
			getEngine().map().getNearbyEntities(entity.getX(), entity.getY(), co.width + co.height, other -> {
				if(other != null && other.collider() != null 
						&& other.getType().collide(entity, other) 
						&& entity.getType().collide(other, entity) 
						&& entity.collider().getBounds(entity).overlaps(other.collider().getBounds(other))){
					handler.dispatchEvent(entity, other);
				}
			});
			
			//TODO server-side collision checking?
			correctPosition(entity, entity.collider());
		}
	}

	public void correctPosition(KoruEntity entity, ColliderComponent comp){
		vector.set(0, 0);
		Rectangle rect = comp.getTerrainBounds(entity);

		checkTerrain(entity.getX(), entity.getY(), out -> {
			
			Vector2 vec = Physics.overlap(rect, out);
			vec.scl(3f);
			rect.x += vec.x;
			rect.y += vec.y;
			vector.add(vec);
		});

		entity.pos().add(vector.x, vector.y);
	}

	void checkTerrain(float x, float y, Consumer<Rectangle> cons){
		World world = World.instance();
		int tilex = World.tile(x);
		int tiley = World.tile(y);
		int range = 1;
		for(int rx = -range; rx <= range; rx++){
			for(int ry = -range; ry <= range; ry++){
				int worldx = tilex + rx, worldy = tiley + ry;
				if(!world.inClientBounds(worldx, worldy))
					continue;
				Tile tile = world.getTile(worldx, worldy);
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
				if(!world.inClientBounds(worldx, worldy))
					continue;
				Tile tile = world.getTile(worldx, worldy);
				if(!tile.solid())
					continue;

				Rectangle out = tile.solidMaterial().getType().getHitbox(worldx, worldy, Rectangle.tmp2);

				if(out.overlaps(rect))
					return true;
			}
		}

		return false;
	}
}
