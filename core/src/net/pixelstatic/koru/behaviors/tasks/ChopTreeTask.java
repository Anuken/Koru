package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;

import com.badlogic.gdx.math.Vector2;

public class ChopTreeTask extends Task{
	private int searchrange = 50;

	@Override
	protected void update(){
		World world = KoruUpdater.instance.world;
		int ex= World.tile(entity.position().x);
		int ey = World.tile(entity.position().y);
		int nearestx = -9999, nearesty = -9999;
		float closest = Float.MAX_VALUE;
		for(int x = -searchrange; x <= searchrange; x ++){
			for(int y = -searchrange; y <= searchrange; y ++){
				int worldx = ex + x, worldy = ey + y;
				if(!World.inBounds(worldx, worldy)) continue;
				if(world.tiles[worldx][worldy].block.breakable()){
					float dist = Vector2.dst(0, 0, x, y);
					if(dist < closest && !Group.instance.blockReserved(worldx, worldy)){
						nearestx = x;
						nearesty = y;
						closest = dist;
					}
				}
			}
		}
		if(nearestx == -9999 || nearesty == -9999){
			finish();
			return;
		}
		int targetx = ex+nearestx;
		int targety = ey+nearesty;
		Group.instance.reserveBlock(targetx, targety);
		this.insertTask(new BreakBlockTask(targetx, targety));
		this.insertTask(new MoveTowardTask(targetx*12+6, targety*12+6));
	}
}
