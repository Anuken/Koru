package net.pixelstatic.koru.behaviors.groups;

import java.awt.Point;

import net.pixelstatic.koru.behaviors.tasks.PlaceBlockTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.world.Material;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class Structure{
	private Array<Task> tasks = new Array<Task>();
	private int x = 20, y = 20;
	private StructureType schematic;
	private int[][] blocks;
	private Array<Point> points = new Array<Point>();

	public Structure(StructureType schematic, int x, int y){
		this.x = x;
		this.schematic = schematic;
		this.y = y;
		blocks = schematic.getTiles();
		addBuildTasks();
	}

	public boolean isDone(){
		return tasks.size == 0;
	}

	public Task getTask(){
		return tasks.pop();
	}
	/*
	public Array<Point> getBlocks(int type){
		clearPoints();
		for(int x = 0;x < blocks.length;x ++){
			for(int y = 0;y < blocks[x].length;y ++){
				if(blocks[x][y] == type){
					Point point = Pools.get(Point.class).obtain();
					point.setLocation(x+this.x, y+this.y);
					points.add(point);
				}
			}
		}
		return points;
	}
	
	*/
	public void clearPoints(){
		Pools.freeAll(points);
		points.clear();
	}
	
	public StructureType getType(){
		return schematic;
	}

	private void addBuildTasks(){
		for(int x = 0;x < blocks.length;x ++){
			for(int y = 0;y < blocks[x].length;y ++){
				int worldx = this.x + x, worldy = this.y + y;
				int id = blocks[x][y];
				Material block = null;
				Material floor = null;
				
				if(id == 0){
					floor = Material.woodfloor;
				}else if(id == 1){
					block = Material.woodblock;
				}else if(id == 2){
					block = Material.pinesapling;
				}else if(id == 3){
					block = Material.box;
					floor = Material.woodfloor;
				}
				
				if(block != null) tasks.add(new PlaceBlockTask(worldx, worldy, block));

				if(floor != null) tasks.add(new PlaceBlockTask(worldx, worldy, floor));

			}
		}
	}

}
