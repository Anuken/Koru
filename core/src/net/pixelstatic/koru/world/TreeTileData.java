package net.pixelstatic.koru.world;

import net.pixelstatic.koru.modules.World;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;


public abstract class TreeTileData extends UpdatingTileData{
	public final long growtime;
	public final int maxwater = 20;
	private long growstart = TimeUtils.millis();
	private int timer = maxwater;
	private int water;
	
	public TreeTileData(long growtime){
		this.growtime = 2*1000;//growtime;
	}
	
	public void addWater(){
		if(hasEnoughWater()) return;
		
		water --;
		timer --;
	}

	public void update(int x, int y, Tile tile){
		//if(!hasEnoughWater())return;
		if(timer == 0){
			tile.block.growEvent(tile);
			World.instance().updateTile(x, y);
			water = 0;
			growstart = TimeUtils.millis();
			timer = maxwater;
			
		}
		if(TimeUtils.timeSinceMillis(growstart) > growtime){
			water ++;
			growstart = TimeUtils.millis() + MathUtils.random(-growtime, growtime);
			//tile.block.growEvent(tile);
			//World.instance().updateTile(x, y);
			//growstart = 0;
			//water = 0;
			//Koru.log("Tree has finished growing.");
		}
	}
	
	public boolean hasEnoughWater(){
		return water == 0;
	}
}
