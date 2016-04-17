package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;

public class BreakBlockTask extends Task{
	int blockx, blocky;
	boolean waited = false;
	
	public BreakBlockTask(int x, int y){
		blockx = x;
		blocky = y;
	}
	
	@Override
	protected void update(){
		if(!waited){
			this.insertTask(new WaitTask(60));
			waited = true;
			return;
		}
		if(!World.inBounds(blockx, blocky)){ 
			finish();
			return;
		}
		World world = KoruUpdater.instance.world;
		world.tiles[blockx][blocky].block = Material.air;
		world.updateTile(blockx, blocky);
		entity.mapComponent(InventoryComponent.class).addItem(new ItemStack(Item.wood, 5));
		Group.instance.unreserveBlock(blockx, blocky);
		finish();
	}
}
