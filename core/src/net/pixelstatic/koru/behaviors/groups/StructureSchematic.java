package net.pixelstatic.koru.behaviors.groups;

public enum StructureSchematic{
	//@formatter:off
	house(
		new int[][]{
			{1,1,1,1,1,1},
			{1,0,0,0,0,1},
			{0,0,0,0,0,1},
			{0,0,0,0,0,1},
			{1,0,0,0,0,1},
			{1,1,1,1,1,1},
		}
	), 
	garden(
		new int[][]{
			{1,1,1,1,1,1},
			{1,0,0,0,0,1},
			{0,0,2,2,0,1},
			{0,0,2,2,0,1},
			{1,0,0,0,0,1},
			{1,1,1,1,1,1},
		}
	);
	//@formatter:on
	private int[][] tiles;
	
	private StructureSchematic(int[][] tiles){
		this.tiles = tiles;
	}
	
	public int[][] getTiles(){
		return tiles;
	}
}
