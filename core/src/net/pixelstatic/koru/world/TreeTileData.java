package net.pixelstatic.koru.world;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.modules.World;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;


public abstract class TreeTileData extends UpdatingTileData{
	public final long growtime;
	public final int maxwater = 10;
	private long growstart;
	private int water;
	
	public TreeTileData(long growtime){
		this.growtime = growtime;
	}
	
	public void addWater(){
		if(hasEnoughWater()) return;
		water ++;
		if(hasEnoughWater()){
			Koru.log("Tree has been watered enough.");
			growstart = TimeUtils.millis()  + MathUtils.random(-5000, 5000);
		}
	}

	public void update(int x, int y, Tile tile){
		if(!hasEnoughWater())return;
		
		if(TimeUtils.timeSinceMillis(growstart) > growtime){
			tile.block.growEvent(tile);
			World.instance().updateTile(x, y);
			growstart = 0;
			water = 0;
			Koru.log("Tree has finished growing.");
		}
	}
	
	public boolean hasEnoughWater(){
		return water > maxwater;
	}
}
