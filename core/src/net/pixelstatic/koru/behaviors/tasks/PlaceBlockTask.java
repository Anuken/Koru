package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.MaterialType;

import com.badlogic.gdx.math.Vector2;

public class PlaceBlockTask extends Task{
	final int blockx, blocky;
	final Material material;
	private boolean waited = false;

	public PlaceBlockTask(int x, int y, Material material){
		blockx = x;
		blocky = y;
		this.material = material;
	}

	@Override
	protected void update(){
		World world = KoruUpdater.instance.world;
		if(!World.inBounds(blockx, blocky)){
			finish();
			entity.log("Block index out of bounds.");
			return;
		}
	//	entity.group().reserveBlock(blockx, blocky);
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);
		
		boolean missing = false;
		for(ItemStack stack : material.getDrops()){
			if( !inventory.hasItem(stack)){
				insertTask(new HarvestResourceTask(stack.item, stack.amount*2));
				missing = true;
			}
		}
		if(missing) return;
		
		if(Vector2.dst(entity.getX(), entity.getY(), blockx * 12 + 6, (blocky) * 12 + 6) > MoveTowardTask.completerange){
			insertTask(new MoveTowardTask(blockx, blocky));
			return;
		}else if(world.tiles[blockx][blocky].block != Material.air && 
				(!material.getType().tile() || world.tiles[blockx][blocky].block.getType() == MaterialType.grass  
				|| world.tiles[blockx][blocky].block.getType() == MaterialType.tree)){
			insertTask(new BreakBlockTask(world.tiles[blockx][blocky].block, blockx, blocky));
			return;
		}
		
		if(!waited && material.getType().solid()){
			this.insertTask(new WaitUntilEmptyTask(material, blockx, blocky));
			waited = true;
			return;
		}
			
		
		world.tiles[blockx][blocky].setMaterial(material);
		world.updateTile(blockx, blocky);
		inventory.removeAll(material.getDrops());
		entity.group().registerBlock(entity, material, blockx, blocky);
		entity.group().unreserveBlock(blockx, blocky);
		finish();
	}
}
