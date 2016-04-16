package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;

import com.badlogic.gdx.math.Vector2;

public class ChopTreeTask extends Task{
	private int searchrange = 10;

	@Override
	protected void update(){
		World world = KoruUpdater.instance.world;
		int ex= World.tile(entity.position().x);
		int ey = World.tile(entity.position().y);
		int nearestx = -1, nearesty = -1;
		float closest = Float.MAX_VALUE;
		for(int x = -searchrange; x <= searchrange; x ++){
			for(int y = -searchrange; y <= searchrange; y ++){
				int worldx = ex + x, worldy = ey + y;
				if(!World.inBounds(worldx, worldy)) continue;
				if(world.tiles[worldx][worldy].block.breakable()){
					float dist = Vector2.dst(0, 0, x, y);
					if(dist < closest){
						nearestx = x;
						nearesty = y;
						closest = dist;
					}
				}
			}
		}
		int targetx = ex+nearestx;
		int targety = ey+nearesty;
		this.insertTask(new BreakBlockTask(targetx, targety));
		this.insertTask(new MoveTowardTask(targetx*12+6, targety*12+6));
	}
}
