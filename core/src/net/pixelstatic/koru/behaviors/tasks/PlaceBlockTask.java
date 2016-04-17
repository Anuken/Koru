package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;

import com.badlogic.gdx.math.Vector2;

public class PlaceBlockTask extends Task{
	int blockx, blocky;
	Material material;
	
	public PlaceBlockTask(int x, int y, Material material){
		blockx = x;
		blocky = y;
		this.material = material;
	}
	
	@Override
	protected void update(){
		World world = KoruUpdater.instance.world;
		if(Vector2.dst(entity.getX(), entity.getY(), blockx*12+6, (blocky+2)*12+6) > MoveTowardTask.completerange){
			insertTask(new MoveTowardTask(blockx*12+6, (blocky+2)*12+6));
			return;
		}else if(world.tiles[blockx][blocky].block != Material.air){
			insertTask(new BreakBlockTask(blockx, blocky));
			return;
		}
		if(entity.mapComponent(InventoryComponent.class).quantityOf(Item.wood) < 3){
			finish();
			return;
		}
		world.tiles[blockx][blocky].setMaterial(material);
		world.updateTile(blockx, blocky);
		entity.mapComponent(InventoryComponent.class).removeItem(new ItemStack(Item.wood, 3));
		finish();
	}
}
