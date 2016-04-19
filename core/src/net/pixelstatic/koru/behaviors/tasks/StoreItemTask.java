package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.InventoryTileData;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

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
			//Koru.log("moving to task: " + entity.getID());
			return;
		}

	
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);
		Koru.log("Storing items: " + entity.getID() + " : " + inventory);
		ObjectMap<Item, Integer> stored = world.tiles[blockx][blocky].getBlockData(InventoryTileData.class).inventory.merge(inventory);
		ItemStack temp = new ItemStack();
		for(Item item : stored.keys()){
			temp.set(item, stored.get(item));
			entity.group().updateStorage(world.tiles[blockx][blocky], temp, true);
		}
		world.updateTile(blockx, blocky);
		finish();
	}
}
