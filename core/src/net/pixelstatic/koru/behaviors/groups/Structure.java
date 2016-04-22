package net.pixelstatic.koru.behaviors.groups;

import java.awt.Point;

import net.pixelstatic.koru.behaviors.tasks.PlaceBlockTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.MaterialPair;
import net.pixelstatic.koru.world.Tile;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Structure{
	private Array<Task> tasks = new Array<Task>();
	private final int x, y;
	private StructureType schematic;
	private ObjectMap<Material, Array<Point>> blocks = new ObjectMap<Material, Array<Point>>();
	private Array<KoruEntity> entities = new Array<KoruEntity>();
	private BuildState[][] buildstates;

	public Structure(StructureType schematic, int x, int y){
		this.x = x;
		this.y = y;
		this.schematic = schematic;
		addBuildTasks();
		buildstates = new BuildState[schematic.tiles.length][schematic.tiles[0].length];
		for(int bx = 0; bx < buildstates.length; bx ++){
			for(int by = 0; by < buildstates[0].length; by ++){
				buildstates[bx][by] = BuildState.unbuilt;
			}
		}
	}

	public boolean isDone(){
		return tasks.size == 0;
	}
	
	public Tile getTileAt(int x, int y){
		//Koru.log("returning " + this.x + " [" + x + "] " + "[" + (this.x + x)+"]");
		return World.instance().tiles[this.x + x][this.y + y];
	}
	
	public int worldX(int x){
	//	Koru.log("returning " + (x + this.x));
		return this.x + x;
	}
	
	public int worldY(int y){
		return this.y + y;
	}
	
	public boolean assignable(){
		return schematic.assignable() || !this.isDone();
	}

	public Task getTask(KoruEntity entity){
		if(tasks.size == 0)
			return schematic.getTask(this, entity);
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
	
	public int maxAssignedEntities(){
		return schematic.maxAssignedEntities();
	}
	
	public void assignEntity(KoruEntity entity){
		if(entity.groucp().structure != null) entity.groucp().structure.deassignEntity(entity);
		entities.add(entity);
		entity.groucp().structure = this;
	}
	
	public void deassignEntity(KoruEntity entity){
		entity.groucp().structure = null;
		entities.removeValue(entity, true);
	}
	
	public int assignedEntities(){
		return entities.size;
	}
	
	public boolean isEntityAssigned(KoruEntity entity){
		return entities.contains(entity, true);
	}
	
	public boolean isOverloaded(){
		return maxAssignedEntities() < entities.size;
	}

	public void registerBlock(KoruEntity entity, Material material, int x, int y){
		if( !blocks.containsKey(material)){
			blocks.put(material, new Array<Point>());
		}
		blocks.get(material).add(new Point(x, y));
		buildstates[x - this.x][y - this.y] = BuildState.completed;
	}
	
	public StructureType getType(){
		return schematic;
	}

	private void addBuildTasks(){
		int[][] blocks = schematic.getTiles();
		
		for(int x = 0;x < blocks.length;x ++){
			for(int y = 0;y < blocks[x].length;y ++){
				int worldx = this.x + x, worldy = this.y + y;
				int id = blocks[x][y];
				MaterialPair pair = StructureType.getMaterials(id);
				
				if(pair.block != null) tasks.add(new PlaceBlockTask(worldx, worldy, pair.block));

				if(pair.tile != null) tasks.add(new PlaceBlockTask(worldx, worldy, pair.tile));

			}
		}
	}
	
	public BuildState getBuildState(int x, int y){
		int rx = x - this.x;
		int ry = y - this.y;
		if(rx >= buildstates.length || ry >= buildstates[0].length || rx < 0 || ry < 0) return BuildState.completed;
		return buildstates[rx][ry];
	}
	
	public static enum BuildState{
		unbuilt, completed
	}

}
