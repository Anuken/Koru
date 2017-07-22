package io.anuke.koru.systems;

import static io.anuke.ucore.util.Mathf.scl;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Predicate;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.World;
import io.anuke.koru.traits.ConnectionTrait;
import io.anuke.koru.traits.SyncTrait;
import io.anuke.ucore.ecs.Processor;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.util.GridMap;

public class EntityMapper extends Processor{
	public static final float cellsize = World.tilesize * World.chunksize / 2;
	public static final int maxCells = 3000;
	
	private GridMap<ArrayList<Spark>> map = new GridMap<>();
	private boolean debug = false;

	public static Predicate<Spark> allPredicate = (entity) -> true;

	public int getCellAmount(){
		return map.size();
	}

	/**
	 * Returns all the entities near a specific location (within range).
	 **/
	public synchronized void getNearbyEntities(float cx, float cy, float range, Predicate<Spark> pred,
			Consumer<Spark> con){

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
				ArrayList<Spark> set = map.get(x, y);
				if(set != null){
					for(Spark e : set){
						if(pred.evaluate(e) && Math.abs(e.pos().x - cx) < range && Math.abs(e.pos().y - cy) < range){
							con.accept(e);
						}
					}
				}
			}
		}
	}

	/** Gets nearby entities with a connection */
	public void getNearbyConnections(float cx, float cy, float range, Consumer<Spark> con){
		getNearbyEntities(cx, cy, range, ConnectionTrait.class, con);
	}

	/** Gets nearby syncables. */
	public void getNearbySyncables(float cx, float cy, float range, Consumer<Spark> con){
		getNearbyEntities(cx, cy, range, SyncTrait.class, con);
	}
	
	/** Gets nearby entities with a specific component. */
	public void getNearbyEntities(float cx, float cy, float range, Class<? extends Trait> component, Consumer<Spark> con){
		getNearbyEntities(cx, cy, range, e -> e.has(component), con);
	}

	/** Just gets all nearby entities. */
	public void getNearbyEntities(float cx, float cy, float range, Consumer<Spark> con){
		getNearbyEntities(cx, cy, range, allPredicate, con);
	}

	public ArrayList<Spark> getEntitiesIn(float cx, float cy){
		int x = (int) (cx / cellsize), y = (int) (cy / cellsize);
		return map.get(x, y);
	}
	
	//TODO quadtree for each cell?
	void processEntity(Spark entity){
		int x = (int) (entity.pos().x / cellsize), y = (int) (entity.pos().y / cellsize);

		ArrayList<Spark> set = map.get(x, y);

		if(set == null){
			map.put(x, y, (set = new ArrayList<Spark>()));
		}

		set.add(entity);
	}

	@Override
	public synchronized void update(Array<Spark> sparks){

		// clear cells just in case
		if(map.size() > maxCells){
			Koru.log("Too many mapper cells (" + map.size() + "). Clearing and calling System#gc().");
			map.clear();
			// might as well try to clean up - bad design, maybe?
			System.gc();
		}

		for(ArrayList<Spark> set : map.values()){
			set.clear();
		}
		
		for(Spark spark : sparks){
			processEntity(spark);
		}
	}
}
