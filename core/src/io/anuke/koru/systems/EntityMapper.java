package io.anuke.koru.systems;

import static io.anuke.ucore.UCore.scl;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Predicate;

import io.anuke.koru.Koru;
import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.SyncComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.World;
import io.anuke.ucore.util.GridMap;

public class EntityMapper extends KoruSystem implements EntityListener{
	public static final float cellsize = World.tilesize * World.chunksize / 2;
	public static final int maxCells = 3000;
	protected ObjectMap<Long, KoruEntity> entities = new ObjectMap<Long, KoruEntity>();
	private GridMap<ArrayList<KoruEntity>> map = new GridMap<ArrayList<KoruEntity>>();
	private boolean debug = false;
	
	//TODO remove these nasty predicates and try something cleaner
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

	public int getCellAmount(){
		return map.size();
	}

	/**
	 * Returns all the entities near a specific location (within range).
	 **/
	public synchronized void getNearbyEntities(float cx, float cy, float range, Predicate<KoruEntity> pred,
			Consumer<KoruEntity> con){

		if(range < 1 || range < 1)
			throw new IllegalArgumentException("rangex and rangey cannot be negative.");

		int maxx = scl(cx + range, cellsize), maxy = scl(cy + range, cellsize), minx = scl(cx - range, cellsize),
				miny = scl(cy - range, cellsize);

		if(debug){
			Koru.log("scan position: " + cx + ", " + cy);
			Koru.log("placed quadrant: " + scl(cx, EntityMapper.cellsize) + ", " + scl(cy, EntityMapper.cellsize));
			Koru.log("bounds: " + minx + ", " + miny + "  " + maxx + ", " + maxy);
		}

		for(int x = minx; x < maxx + 1; x++){
			for(int y = miny; y < maxy + 1; y++){
				ArrayList<KoruEntity> set = map.get(x, y);
				if(set != null){
					for(KoruEntity e : set){
						if(pred.evaluate(e) && Math.abs(e.getX() - cx) < range && Math.abs(e.getY() - cy) < range){
							con.accept(e);
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

	public ArrayList<KoruEntity> getEntitiesIn(float cx, float cy){
		int x = (int) (cx / cellsize), y = (int) (cy / cellsize);
		return map.get(x, y);
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		int x = (int) (entity.getX() / cellsize), y = (int) (entity.getY() / cellsize);

		ArrayList<KoruEntity> set = map.get(x, y);

		if(set == null){
			map.put(x, y, (set = new ArrayList<KoruEntity>()));
		}

		set.add(entity);
	}

	@Override
	public synchronized void update(float deltaTime){

		// clear cells just in case
		if(map.size() > maxCells){
			Koru.log("Too many mapper cells (" + map.size() + "). Clearing and calling System#gc().");
			map.clear();
			// might as well try to clean up
			System.gc();
		}

		for(ArrayList<KoruEntity> set : map.values()){
			set.clear();
		}

		super.update(deltaTime);
	}

	@Override
	public void entityAdded(Entity entity){
		entities.put(((KoruEntity) entity).getID(), (KoruEntity) entity);
	}

	@Override
	public void entityRemoved(Entity entity){
		//RenderComponent render = entity.getComponent(RenderComponent.class);
		// don't remove local player
		//if(render != null && !(entity.getComponent(ConnectionComponent.class) != null
		//		&& entity.getComponent(ConnectionComponent.class).local == true))
			//render.renderer.list.free();

		entities.remove(((KoruEntity) entity).getID());
	}
}
