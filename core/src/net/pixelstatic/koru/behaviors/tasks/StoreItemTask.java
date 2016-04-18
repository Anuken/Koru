package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.InventoryTileData;

import com.badlogic.gdx.math.Vector2;

public class StoreItemTask extends Task{
	int blockx, blocky;

	public StoreItemTask(int x, int y){
		this.blockx = x;
		this.blocky = y;
	}

	@Override
	protected void update(){
		World world = KoruUpdater.instance.world;

		if(Vector2.dst(entity.getX(), entity.getY(), blockx * 12 + 6, (blocky) * 12 + 6) > MoveTowardTask.completerange){
			insertTask(new MoveTowardTask(blockx * 12 + 6, (blocky) * 12 + 6));
			return;
		}

		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);

		for(ItemStack[] array : inventory.inventory)
			for(ItemStack stack : array)
				if(stack != null) world.tiles[blockx][blocky].getBlockData(InventoryTileData.class).inventory.addItem(stack);
		world.updateTile(blockx, blocky);
		inventory.clear();
		finish();
	}
}
