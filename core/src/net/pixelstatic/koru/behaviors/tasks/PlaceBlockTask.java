package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.MaterialType;

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
		if(Vector2.dst(entity.getX(), entity.getY(), blockx * 12 + 6, (blocky) * 12 + 6) > MoveTowardTask.completerange){
			insertTask(new MoveTowardTask(blockx * 12 + 6, (blocky) * 12 + 6));
			return;
		}else if(world.tiles[blockx][blocky].block != Material.air && 
				(!material.getType().tile() || world.tiles[blockx][blocky].block.getType() == MaterialType.grass  
				|| world.tiles[blockx][blocky].block.getType() == MaterialType.tree)){
			insertTask(new BreakBlockTask(world.tiles[blockx][blocky].block, blockx, blocky));
			return;
		}
		
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);
		
		boolean missing = false;
		for(ItemStack stack : material.getDrops()){
			if( !inventory.hasItem(stack)){
				insertTask(new HarvestResourceTask(stack.item, stack.amount*2).setIgnoredMaterial(material));
				missing = true;
			}
		}
		if(missing) return;

		world.tiles[blockx][blocky].setMaterial(material);
		world.updateTile(blockx, blocky);
		inventory.removeAll(material.getDrops());
		finish();
	}
}
