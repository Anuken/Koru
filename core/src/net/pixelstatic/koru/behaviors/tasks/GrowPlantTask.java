package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;

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
		Material material = world.tiles[blockx][blocky].block;
		if(!material.growable()){
			finish();
			return;
		}

		if(Vector2.dst(entity.getX(), entity.getY(), blockx * 12 + 6, (blocky) * 12 + 6) > MoveTowardTask.completerange){
			insertTask(new MoveTowardTask(blockx * 12 + 6, (blocky) * 12 + 6));
			return;
		}

		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);

		if( !inventory.hasItem(material.getGrowItem())){
			insertTask(new HarvestResourceTask(material.getGrowItem().item, material.getGrowItem().amount * 2));
			return;
		}
		world.tiles[blockx][blocky].block.growEvent(world.tiles[blockx][blocky]);
		world.updateTile(blockx, blocky);
		inventory.removeItem(material.getGrowItem());
		finish();
	}

}
