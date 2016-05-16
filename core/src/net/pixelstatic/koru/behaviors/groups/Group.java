package net.pixelstatic.koru.behaviors.groups;

import net.pixelstatic.koru.behaviors.tasks.PlaceBlockTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.behaviors.tasks.Task.FailReason;
import net.pixelstatic.koru.components.GroupComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.utils.Point;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.Tile;
import net.pixelstatic.koru.world.World;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Group{
	private static Group instance;
	private Array<Point> reservedblocks = new Array<Point>();
	protected Array<KoruEntity> entities = new Array<KoruEntity>();
	private Array<GroupModule> modules = new Array<GroupModule>();
	protected int x = 10, y = 10;
	private ObjectMap<Material, Array<Point>> blocks = new ObjectMap<Material, Array<Point>>();
	private ObjectMap<Item, Integer> resources = new ObjectMap<Item, Integer>();
	private ObjectMap<PointType, Array<TilePoint>> points = new ObjectMap<PointType, Array<TilePoint>>();
	private Array<Point> structures = new Array<Point>();

	static{
		instance = new Group();
	}

	public static Group instance(){
		return instance;
	}

	public Group(){
		for(PointType type : PointType.values())
			points.put(type, new Array<TilePoint>());
		module(new StorageModule());
		module(new TreeFarmModule());

	}
	
	public boolean structureTaken(int x, int y){
		for(Point point : structures){
			if(point.equals(x,y))
				return true;
		}
		return false;
	}
	
	public void addStructure(int x, int y){
		structures.add(new Point(x,y));
	}

	private void module(GroupModule module){
		modules.add(module);
	}

	int pointAmount(PointType type){
		return points.get(type).size;
	}

	public Array<TilePoint> points(PointType type){
		return points.get(type);
	}

	void addPoint(PointType type, int x, int y){
		points.get(type).add(new TilePoint(x, y));
	}

	public void update(){
		for(GroupModule module : modules){
			module.updateInternal(this);
		}
	}

	public Task getTask(KoruEntity entity){

		for(GroupModule module : modules){
			//if(module.hasTasks()){
				Task task = module.getTask(entity);
				if(task != null){
					module.taskTakeEvent();
					return task;
				}
			//}
		}

		/*
		int trees = points(PointType.treefarm);
		if(trees < 6){ // no tree farms
			if(trees == 0){
				Point water = world.search(Material.water, entity.position().blockX(), entity.position().blockY(), 50); //search for water
				if(water == null) return null;
				Point point = world.findEmptySpace(water.x, water.y);
				if(point == null) return null; //no empty spot found?

				addPoint(PointType.treefarm, point.x, point.y);
			}else{
				Array<TilePoint> points = this.points.get(PointType.treefarm);
				int min = Integer.MAX_VALUE;
				int y = 0;
				for(TilePoint point : points){
					y = point.y;
					min = Math.min(point.x, min);
				}
				//System.out.println(min-1);
				addPoint(PointType.treefarm, min - 1, y);

			}
		}else{
			Array<TilePoint> points = this.points.get(PointType.treefarm);
			for(TilePoint point : points){
				if(point.reserved()) continue;
				Material material = point.block();

				if(material == Material.pinesapling && !point.tile().getBlockData(PinetreeTileData.class).hasEnoughWater()){
					return new GrowPlantTask(point.x, point.y);
				}else if(material.name().contains("pinetree")){
					return new BreakBlockTask(material, point.x, point.y);
				}else{
					return new PlaceBlockTask(point.x, point.y, Material.pinesapling);
				}
				//if(point.state == PointState.used){

				//}else{

				//}
			}
		}
		*/
		return null;
		//return new HarvestResourceTask(Item.stone, 1, false);
	}

	/*
		private Task getDefaultTask(KoruEntity entity){
			InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);

			

			if(inventory.usedSlots() > 3){
				Array<Point> chests = getBlocks(Material.box);
				for(Point point : chests){
					if( !World.instance().getTile(point).getBlockData(InventoryTileData.class).inventory.full()) return new StoreItemTask(point.x, point.y);
				}
				storageFailEvent();
			}
			return null;
		}
		
		*/

	public void updateStorage(Tile tile, ItemStack stack, boolean add){
		if( !resources.containsKey(stack.item)) resources.put(stack.item, 0);
		resources.put(stack.item, resources.get(stack.item) + (add ? 1 : -1) * stack.amount);
	}

	public int resourceAmount(Item item){
		if( !resources.containsKey(item)) resources.put(item, 0);
		return resources.get(item);
	}

	public int tileAmount(Material material){
		if( !blocks.containsKey(material)) blocks.put(material, new Array<Point>());
		return blocks.get(material).size;
	}

	public void registerBlock(KoruEntity entity, Material material, int x, int y){
		if( !blocks.containsKey(material)) blocks.put(material, new Array<Point>());
		Point point = new Point(x, y);
		blocks.get(material).add(point);
	}

	public boolean isBaseBlock(Material material, int x, int y){
		Array<Point> tiles = getBlocks(material);
		for(Point point : tiles){
			if(point.x == x && point.y == y){
				return true;
			}
		}
		return false;
	}

	public Array<Point> getBlocks(Material material){
		if( !blocks.containsKey(material)){
			blocks.put(material, new Array<Point>());
		}
		return blocks.get(material);
	}

	public boolean isGroupBlock(Material material, int x, int y){
		return false;
	}

	public void addEntity(KoruEntity entity){
		entities.add(entity);
	}

	public void notifyTaskFailed(KoruEntity entity, Task task, FailReason reason){
		task.behavior.resetTimer();
		if(task instanceof PlaceBlockTask){
			
			entity.log("place block task failed!");
		}
	}

	public boolean blockReserved(int x, int y){
		for(Point point : reservedblocks){
			if(point.x == x && point.y == y){
				return true;
			}
		}
		return false;
	}

	public void unreserveBlock(int x, int y){
		for(Point point : reservedblocks){
			if(point.x == x && point.y == y){
				reservedblocks.removeValue(point, true);
				break;
			}
		}
	}

	public void reserveBlock(int x, int y){
		reservedblocks.add(new Point(x, y));
	}

	public static KoruEntity createGroupEntity(Group group){
		KoruEntity entity = new KoruEntity(EntityType.group);
		entity.mapComponent(GroupComponent.class).group = group;
		return entity;
	}

	static enum PointType{
		treefarm
	}

	static enum PointState{
		none, used
	}

	class TilePoint{
		public final int x, y;
		public PointState state = PointState.none;

		public Material block(){
			return World.instance().tiles[x][y].block;
		}

		public Tile tile(){
			return World.instance().tiles[x][y];
		}

		public boolean reserved(){
			return blockReserved(x, y);
		}

		public TilePoint(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
}
