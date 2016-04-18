package net.pixelstatic.koru.behaviors.groups;

import java.awt.Point;

import net.pixelstatic.koru.behaviors.tasks.Task;

import com.badlogic.gdx.utils.Array;

public class Group{
	public static Group instance;
	private Array<Point> usedtasks = new Array<Point>();
	private Array<Structure> structures = new Array<Structure>();

	static{
		instance = new Group();
	}

	public Group(){
		structures.add(new Structure(StructureSchematic.garden, 20, 20));
		structures.add(new Structure(StructureSchematic.house, 30, 20));
		structures.add(new Structure(StructureSchematic.house, 40, 20));
		//addBuilding(x, y);
	}
	
	private void addNewBuildings(){
		
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
		addNewBuildings();
		return null;
	}
}
