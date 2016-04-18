package net.pixelstatic.koru.behaviors.groups;

import java.awt.Point;

import net.pixelstatic.koru.behaviors.tasks.*;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.Tile;

import com.badlogic.gdx.utils.Array;

public class Group{
	private static Group instance;
	private Array<Point> usedtasks = new Array<Point>();
	private Array<Structure> structures = new Array<Structure>();
	private Array<Point> points = new Array<Point>();
	private Array<KoruEntity> entities = new Array<KoruEntity>();

	static{
		instance = new Group();
	}
	
	public static Group instance(){
		return instance;
	}

	public Group(){
		structures.add(new Structure(StructureType.garden, 20, 20));
		structures.add(new Structure(StructureType.storage, 20, 30));
		
		//structures.add(new Structure(StructureSchematic.house, 30, 20));
		//structures.add(new Structure(StructureSchematic.house, 40, 20));
		//addBuilding(x, y);
	}
	
	public void addEntity(KoruEntity entity){
		entities.add(entity);
	}
	
	private Task getDefaultTask(){
		for(Structure structure : structures){ // get all shrub blocks
			points.addAll(structure.getBlocks(2));
		}
		for(Point point : points){
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
		return null;
	}
	
	public int amountOfStructure(StructureType schematic){
		int amount = 0;
		for(Structure structure : structures){
			if(structure.getType() == schematic)
				amount ++;
		}
		return amount;
	}
	
	public boolean blockReserved(int x, int y){
		for(Point point : usedtasks){
			if(point.x == x && point.y == y){
				return true;
			}
		}
		return false;
	}
	
	public void unreserveBlock(int x, int y){
		for(Point point : usedtasks){
			if(point.x == x && point.y == y){
				usedtasks.removeValue(point, true);
				break;
			}
		}
	}
	
	public void reserveBlock(int x, int y){
		usedtasks.add(new Point(x,y));
	}
	
	public Task getTask(){
		for(Structure structure : structures){
			if(!structure.isDone()){
				return structure.getTask();
			}
		}
		return getDefaultTask();
	}
}
