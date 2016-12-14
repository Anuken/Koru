package io.anuke.koru.systems;

import static io.anuke.ucore.UCore.scl;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Predicate;

import io.anuke.koru.Koru;
import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.components.SyncComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.World;
import io.anuke.ucore.util.GridMap;

public class EntityMapper extends KoruSystem implements EntityListener{
	public static final float gridsize = World.tilesize * World.chunksize * 2;
	protected ObjectMap<Long, KoruEntity> entities = new ObjectMap<Long, KoruEntity>();
	private GridMap<Array<KoruEntity>> map = new GridMap<Array<KoruEntity>>();
	private boolean debug = false;
	private Object lock = new Object();

	public static Predicate<KoruEntity> connectionPredicate = (entity) -> {
		return entity.hasComponent(ConnectionComponent.class);
	};

	public static Predicate<KoruEntity> syncedPredicate = (entity) -> {
		return entity.hasComponent(SyncComponent.class);
	};

	public static Predicate<KoruEntity> allPredicate = (entity) -> {
		return true;
	};

	public EntityMapper() {
		super(Family.all(PositionComponent.class).get());
	}

	/**
	 * Returns all the entities near a specific location (within range).
	 **/
	public void getNearbyEntities(float cx, float cy, float range, Predicate<KoruEntity> pred,
			Consumer<KoruEntity> con){

		synchronized(lock){
			if(range < 1 || range < 1)
				throw new IllegalArgumentException("rangex and rangey cannot be negative.");

			int maxx = scl(cx + range, gridsize), maxy = scl(cy + range, gridsize), minx = scl(cx - range, gridsize),
					miny = scl(cy - range, gridsize);

			if(debug){
				Koru.log("scan position: " + cx + ", " + cy);
				Koru.log("placed quadrant: " + scl(cx, EntityMapper.gridsize) + ", " + scl(cy, EntityMapper.gridsize));
				Koru.log("bounds: " + minx + ", " + miny + "  " + maxx + ", " + maxy);
			}

			for(int x = minx; x < maxx + 1; x++){
				for(int y = miny; y < maxy + 1; y++){
					Array<KoruEntity> set = map.get(x, y);
					if(set != null){
						for(KoruEntity e : set){
							if(e == null)
								Koru.log("wow, a null entity");
							if(pred.evaluate(e) && Math.abs(e.getX() - cx) < range && Math.abs(e.getY() - cy) < range){
								con.accept(e);
							}
						}
					}
				}
			}
		}
	}

	/** Gets nearby entities with a connection */
	public void getNearbyConnections(float cx, float cy, float range, Consumer<KoruEntity> con){
		getNearbyEntities(cx, cy, range, connectionPredicate, con);
	}

	/** Gets nearby syncables. */
	public void getNearbySyncables(float cx, float cy, float range, Consumer<KoruEntity> con){
		getNearbyEntities(cx, cy, range, syncedPredicate, con);
	}

	/** Just gets all nearby entities. */
	public void getNearbyEntities(float cx, float cy, float range, Consumer<KoruEntity> con){
		getNearbyEntities(cx, cy, range, allPredicate, con);
	}

	public Array<KoruEntity> getEntitiesIn(float cx, float cy){
		int x = (int) (cx / gridsize), y = (int) (cy / gridsize);
		return map.get(x, y);
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		int x = (int) (entity.getX() / gridsize), y = (int) (entity.getY() / gridsize);

		Array<KoruEntity> set = map.get(x, y);

		if(set == null){
			map.put(x, y, (set = new Array<KoruEntity>()));
		}

		set.add(entity);
	}

	@Override
	public void update(float deltaTime){
		synchronized(lock){
			for(Array<KoruEntity> set : map.values()){
				set.clear();
			}

			super.update(deltaTime);
		}
	}

	@Override
	public void entityAdded(Entity entity){
		entities.put(((KoruEntity) entity).getID(), (KoruEntity) entity);
	}

	@Override
	public void entityRemoved(Entity entity){
		RenderComponent render = entity.getComponent(RenderComponent.class);
		// don't remove local player
		if(render != null && !(entity.getComponent(ConnectionComponent.class) != null
				&& entity.getComponent(ConnectionComponent.class).local == true))
			render.group.free();

		entities.remove(((KoruEntity) entity).getID());
	}
}
