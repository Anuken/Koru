package net.pixelstatic.koru.behaviors.groups;

import java.awt.Point;

import net.pixelstatic.koru.behaviors.tasks.Task;

import com.badlogic.gdx.utils.Array;

public class Group{
	public static Group instance;
	//private Array<PlaceBlockTask> taskpool = new Array<PlaceBlockTask>();
	private Array<Point> usedtasks = new Array<Point>();
	private Array<Structure> structures = new Array<Structure>();
	//private int x = 20, y = 20;
	//@formatter:off
	/*
	int[][] blocks = {
		{1,1,1,1,1,1},
		{1,0,0,0,0,1},
		{0,0,0,0,0,1},
		{0,0,0,0,0,1},
		{1,0,0,0,0,1},
		{1,1,1,1,1,1},
	};
	//@formatter:on
	 * */
	 

	static{
		instance = new Group();
	}

	public Group(){
		structures.add(new Structure(20, 20));
		structures.add(new Structure(20, 30));
		structures.add(new Structure(20, 40));
		//addBuilding(x, y);
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
		return null;
	}
}
