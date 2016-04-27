package net.pixelstatic.koru.behaviors.groups;

import java.awt.Point;

import net.pixelstatic.koru.behaviors.tasks.*;
import net.pixelstatic.koru.behaviors.tasks.Task.FailReason;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.world.InventoryTileData;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.Tile;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Group{
	private static Group instance;
	private Array<Point> reservedblocks = new Array<Point>();
	private Array<KoruEntity> entities = new Array<KoruEntity>();
	private int offsetx = 10, offsety = 10;
	private ObjectMap<Material, Array<Point>> blocks = new ObjectMap<Material, Array<Point>>();
	private ObjectMap<Item, Integer> resources = new ObjectMap<Item, Integer>();

	static{
		instance = new Group();
	}

	public static Group instance(){
		return instance;
	}

	public Group(){
		//structures.add(new Structure(StructureType.garden, 20, 20));


		//addStructure(StructureType.storage);
		//structures.add(new Structure(StructureSchematic.house, 30, 20));
		//structures.add(new Structure(StructureSchematic.house, 40, 20));
		//addBuilding(x, y);
	}

	private void storageFailEvent(){
		
	}

	
	public Task getTask(KoruEntity entity){

		return new HarvestResourceTask(Item.stone, 1, false);
	}

	private Task getDefaultTask(KoruEntity entity){
		//if(entity.groucp().structure.isOverloaded()) entity.log("Structure overloaded: " + entity.groucp().structure.assignedEntities());
		
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

	public void updateStorage(Tile tile, ItemStack stack, boolean add){
		if( !resources.containsKey(stack.item)) resources.put(stack.item, 0);
		resources.put(stack.item, resources.get(stack.item) + (add ? 1 : -1) * stack.amount);
	}

	public int resourceAmount(Item item){
		if( !resources.containsKey(item)) resources.put(item, 0);
		return resources.get(item);
	}

	public void registerBlock(KoruEntity entity, Material material, int x, int y){
		if( !blocks.containsKey(material)){
			blocks.put(material, new Array<Point>());
		}
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

}
