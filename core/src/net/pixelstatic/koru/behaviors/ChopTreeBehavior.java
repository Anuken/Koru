package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.components.VelocityComponent;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.World;

import com.badlogic.gdx.math.Vector2;

public class ChopTreeBehavior extends Behavior{
	private int targetx=-1, targety=-1;
	private int searchrange = 10;
	private boolean broke = false;
	
	@Override
	protected void update(){
		if(targetx == -1 || targety == -1){
			targetTree();
			return;
		}
		
		float wtx = targetx*World.tilesize+6;
		float wty = targety*World.tilesize+6;
		
		entity.mapComponent(VelocityComponent.class).velocity.set(wtx - entity.getX(), wty - entity.getY())
		.setLength(0.5f);
		
		float dist = Vector2.dst(entity.getX(), entity.getY(), wtx, wty);
		if(dist < 5){
			if(!broke){
				component().insertBehavior(0, new BlockingBehavior(100));
				broke = true;
				return;
			}
			World world = KoruUpdater.instance.world;
			//world.tiles[targetx][targety].block = Material.air;
			world.updateTile(targetx, targety);
			broke = false;
			
			entity.mapComponent(InventoryComponent.class).addItem(new ItemStack(Item.wood, 5));
			targetTree();
			if(entity.mapComponent(InventoryComponent.class).quantityOf(Item.wood) > 40){
				this.component().insertBehavior(0, new GroupBehavior());
				removeSelf();
			}
		}
	}
	
	public ChopTreeBehavior(){
	//	
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
				//if(!World.inBounds(worldx, worldy)) continue;
				//if(world.tiles[worldx][worldy].block.breakable()){
					float dist = Vector2.dst(0, 0, x, y);
					if(dist < closest){
						nearestx = x;
						nearesty = y;
						closest = dist;
					}
				//}
			}
		}
		targetx = ex+nearestx;
		targety = ey+nearesty;
	}
}
