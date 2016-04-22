package net.pixelstatic.koru.behaviors.groups;

import net.pixelstatic.koru.behaviors.tasks.*;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.world.*;

import com.badlogic.gdx.utils.Array;

public enum StructureType{
	//@formatter:off
	house(
		new int[][]{
			{1,1,1,1,1,1},
			{1,0,0,0,0,1},
			{0,0,0,0,0,0},
			{0,0,0,0,0,0},
			{1,0,0,0,0,1},
			{1,1,1,1,1,1},
		}
	), 
	garden(
		new int[][]{
			{1,1,1,1,1,1},
			{1,0,0,0,0,1},
			{0,0,2,2,0,0},
			{0,0,2,2,0,0},
			{1,0,0,0,0,1},
			{1,1,1,1,1,1},
		}
	){
		public Task getTask(Structure structure, KoruEntity entity){
			for(int x = 0; x < tiles.length; x ++){
				for(int y = 0; y < tiles[x].length; y ++){
					if(entity.group().blockReserved(structure.worldX(x), structure.worldX(y))) continue;
					if(tiles[x][y] == 2){
						Tile tile = structure.getTileAt(x, y);
						Task task = checkTask(entity, tile, structure.worldX(x), structure.worldY(y));
						if(task != null){
						//	entity.group().reserveBlock(structure.worldX(x), structure.worldY(y));
							return task;
						}
					}
				}
			}
			return null;
		}
		
		private Task checkTask(KoruEntity entity, Tile tile, int x, int y){
			if(tile.block == Material.air){
				entity.group().reserveBlock(x,y);
				return new PlaceBlockTask(x, y, Material.pinesapling);
			}else if(tile.block == Material.pinesapling){
				if(!tile.getBlockData(TreeTileData.class).hasEnoughWater())
				return new GrowPlantTask(x,y);
			}else if(tile.block.name().contains("pinetree")){
				entity.group().reserveBlock(x,y);
				return new BreakBlockTask(tile.block, x,y);
			}
			return new HarvestResourceTask(Item.stone, 5);
			
			//return null;
		}
	},
	storage(
			new int[][]{
				{1,1,1,1,1,1},
				{1,0,0,0,0,1},
				{0,0,3,3,0,0},
				{0,0,3,3,0,0},
				{1,0,0,0,0,1},
				{1,1,1,1,1,1},
			}
	){
		public boolean assignable(){
			return false;
		}
	};
	//@formatter:on
	private static MaterialPair pair = new MaterialPair();
	protected int[][] tiles;
	private Array<ItemStack> requirements = new Array<ItemStack>();
	
	public Task getTask(Structure structure, KoruEntity entity){

		return null;
	}
	
	public int maxAssignedEntities(){
		return 5;
	}
	
	public Array<ItemStack> getRequirements(){
		return requirements;
	}
	
	private StructureType(int[][] tiles){
		this.tiles = tiles;
		for(int x = 0; x < tiles.length; x ++){
			for(int y = 0; y < tiles[x].length; y ++){
				
			}
		}
	}
	
	public int[][] getTiles(){
		return tiles;
	}
	
	public boolean assignable(){
		return true;
	}
	
	public static MaterialPair getMaterials(int id){
		pair.clear();
		if(id == 0){
			pair.tile = Material.woodfloor;
		}else if(id == 1){
			pair.block = Material.woodblock;
		}else if(id == 2){
			pair.block = Material.pinesapling;
			pair.tile = Material.grass;
		}else if(id == 3){
			pair.block = Material.box;
			pair.tile = Material.woodfloor;
		}
		return pair;
	}
}
