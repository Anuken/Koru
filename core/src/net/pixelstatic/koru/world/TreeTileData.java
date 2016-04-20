package net.pixelstatic.koru.world;

public class TreeTileData extends TileData{
	public final long maxgrowtime;
	public final int maxwater = 10;
	int water;
	
	
	public TreeTileData(long growtime){
		this.maxgrowtime = growtime;
	}
	
	public void water(){
		
	}
	
	public boolean hasEnoughWater(){
		return water > 10;
	}
}
