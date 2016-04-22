package net.pixelstatic.koru.behaviors.groups;

import java.awt.Point;

import net.pixelstatic.koru.behaviors.tasks.StoreItemTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
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
	private Array<Structure> structures = new Array<Structure>();
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

		addStructure(StructureType.garden);
		//addStructure(StructureType.storage);
		//structures.add(new Structure(StructureSchematic.house, 30, 20));
		//structures.add(new Structure(StructureSchematic.house, 40, 20));
		//addBuilding(x, y);
	}

	private void addStructure(StructureType type){
		Structure structure = new Structure(type, offsetx + 10 * (structures.size % 4), offsety + (structures.size / 4) * 10);
		structures.add(structure);
	}

	private void storageFailEvent(){
		if(amountOfUnbuiltStructure(StructureType.storage) != 0) return;
		addStructure(StructureType.storage);
	}

	private void assignStructure(KoruEntity entity){
		for(Structure structure : structures){
			if(structure.assignedEntities() <= structure.maxAssignedEntities() && structure.assignable()){
				structure.assignEntity(entity);
				return;
			}
		}
		
		for(int i = structures.size - 1; i >= 0; i ++){
			Structure structure = structures.get(i);
			if(i == 0 || (structure.assignable() && !structure.isOverloaded())){
				structure.assignEntity(entity);
				return;
			}
		}
	}

	private void checkStructure(KoruEntity entity){
		if(entity.groucp().structure.isOverloaded() || !entity.groucp().structure.assignable()){
			for(Structure structure : structures){
				if(entity.groucp().structure != structure && !structure.isOverloaded() && structure.assignable()){
					structure.assignEntity(entity);
					return;
				}
			}
			structures.first().assignEntity(entity);
		}
		
		for(Structure structure : structures){
			if(!structure.isDone() && !structure.isOverloaded() && Math.random() < 0.05){
				structure.assignEntity(entity);
				return;
			}		
		}
	}

	public Task getTask(KoruEntity entity){
		if(entity.groucp().structure == null){
			assignStructure(entity);
		}else{
			checkStructure(entity);
		}

		Task task = getDefaultTask(entity);
		if(task != null) return task;

		return entity.groucp().structure.getTask(entity);
	}

	private Task getDefaultTask(KoruEntity entity){
		
		if( !entity.groucp().structure.isDone()) return null;
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);

		if(entity.groucp().structure.getType() == StructureType.garden && entity.groucp().structure.isOverloaded() && entity.groucp().structure.isDone() && resourceAmount(Item.wood) > 200 && amountOfUnbuiltStructure(StructureType.garden) == 0){
			entity.log("Adding garden. Structure entities: " + entity.groucp().structure.assignedEntities());
			addStructure(StructureType.garden);
			return null;
		}

		if(inventory.usedSlots() > 3){
			Array<Point> chests = getBlocks(Material.box);
			for(Point point : chests){
				if( !World.instance().getTile(point).getBlockData(InventoryTileData.class).inventory.full()) return new StoreItemTask(point.x, point.y);
			}
			storageFailEvent();
		}
/*
		Array<Point> shrubs = getBlocks(Material.pinesapling);
		for(Point point : shrubs){
			Tile tile = World.instance().getTile(point);
			if(blockReserved(point.x, point.y)) continue;
			if(tile.block == Material.pinesapling){
				return new GrowPlantTask(point.x, point.y);
			}else if(tile.block.name().contains("pinetree")){
				reserveBlock(point.x, point.y);
				return new BreakBlockTask(null, point.x, point.y);
			}else{
				return new PlaceBlockTask(point.x, point.y, Material.pinesapling);
			}
		}
		*/
		return null;
	}

	public void updateStorage(Tile tile, ItemStack stack, boolean add){
		//if(stack.amount == 0) return;
		//	if(KoruUpdater.frameID() % 5 == 0)
		//		Koru.log(resources);
		//if(stack.item == Item.wood) wood += stack.amount;
		//Koru.log("wood: " + wood);
		//Koru.log((add ? "ADDING" : "REMOVING") + " [" + stack.toString() + "]");
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
		
		if(entity.groucp().structure != null)
			entity.groucp().structure.registerBlock(entity, material, x, y);
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

	public int amountOfStructure(StructureType schematic){
		int amount = 0;
		for(Structure structure : structures){
			if(structure.getType() == schematic) amount ++;
		}
		return amount;
	}

	public int amountOfBuiltStructure(StructureType schematic){
		int amount = 0;
		for(Structure structure : structures){
			if(structure.getType() == schematic && structure.isDone()) amount ++;
		}
		return amount;
	}

	public int amountOfUnbuiltStructure(StructureType schematic){
		int amount = 0;
		for(Structure structure : structures){
			if(structure.getType() == schematic && !structure.isDone()) amount ++;
		}
		return amount;
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
