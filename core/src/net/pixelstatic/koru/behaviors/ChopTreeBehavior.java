package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.components.VelocityComponent;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;

import com.badlogic.gdx.math.Vector2;

public class ChopTreeBehavior extends Behavior{
	private int targetx, targety;
	private int searchrange;
	
	@Override
	protected void update(){
		if(targetx == -1 || targety == -1) return;
		
		float wtx = targetx*World.tilesize;
		float wty = targety*World.tilesize;
		
		entity.mapComponent(VelocityComponent.class).velocity.set(wtx - entity.getX(), wty - entity.getY())
		.setLength(0.2f);
		
		float dist = Vector2.dst(entity.getX(), entity.getY(), wtx, wty);
		if(dist < 6){
			World world = KoruUpdater.instance.world;
			world.tiles[targetx][targety].block = Material.air;
		}
	}
	
	public ChopTreeBehavior(){
		targetTree();
	}
	
	private void targetTree(){
		World world = KoruUpdater.instance.world;
		int ex= World.tile(entity.position().x);
		int ey = World.tile(entity.position().y);
		int nearestx = -1, nearesty = -1;
		float closest = Float.MAX_VALUE;
		for(int x = -searchrange; x <= searchrange; x ++){
			for(int y = -searchrange; y <= searchrange; y ++){
				int worldx = ex + x, worldy = ey + y;
				if(!World.inBounds(worldx, worldy)) continue;
				if(world.tiles[x][y].block.breakable()){
					float dist = Vector2.dst(0, 0, x, y);
					if(dist < closest){
						nearestx = x;
						nearesty = y;
						closest = dist;
					}
				}
			}
		}
		targetx = nearestx;
		targety = nearesty;
	}
}
