package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;

public class BreakBlockTask extends Task{
	int blockx, blocky;
	Material material;
	boolean waited = false;
	
	public BreakBlockTask(Material material, int x, int y){
		blockx = x;
		blocky = y;
		this.material = material;
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
		
		material.harvestEvent(world.tiles[blockx][blocky]);
		world.updateTile(blockx, blocky);
		entity.mapComponent(InventoryComponent.class).addItems(material.getDrops());
		Group.instance.unreserveBlock(blockx, blocky);
		finish();
	}
}
