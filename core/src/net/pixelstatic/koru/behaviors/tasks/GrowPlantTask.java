package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.World;

import com.badlogic.gdx.math.Vector2;

public class GrowPlantTask extends Task{
	int blockx, blocky;

	public GrowPlantTask(int x, int y){
		this.blockx = x;
		this.blocky = y;
	}

	@Override
	protected void update(){
		World world = KoruUpdater.instance.world;
		//Material material = world.tiles[blockx][blocky].block;
		//if(!material.growable()){
			//Koru.log("Critical error: lel");
		//	finish();
		//	return;
		//}
		
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);
		
		ItemStack water = new ItemStack(Item.water, 1);
		if( !inventory.hasItem(water)){
			insertTask(new HarvestResourceTask(water.item, water.amount * 2));
			return;
		}

		if(Vector2.dst(entity.getX(), entity.getY(), blockx * 12 + 6, (blocky) * 12 + 6) > MoveTowardTask.completerange){
			insertTask(new MoveTowardTask(blockx, blocky));
			return;
		}
		
	//	world.tiles[blockx][blocky].getBlockData(TreeTileData.class).addWater();
		world.updateTile(blockx, blocky);
		inventory.removeItem(water);
		finish();
	}

}
