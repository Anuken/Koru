package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.world.Material;



public class PlaceBlockTask extends Task{
	
	final int x, y;
	final Material material;
	//private boolean waited = false;

	public PlaceBlockTask(int x, int y, Material material){
		this.x = x;
		this.y = y;
		this.material = material;
	}
	
	public void notifyFailed(Task task, FailReason reason){
		if(task instanceof MoveTowardTask && reason == FailReason.stuck){
			finish(FailReason.stuck);
			entity.position().y ++;
		}
	}

	@Override
	protected void update(){
		/*
		World world = KoruUpdater.instance.world;
		if(!World.inBounds(x, y)){
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
		
		if(world.tiles[x][y].block == material || world.tiles[x][y].tile == material){
			finish();
			return;
		}
		
		int blockx = x, blocky = y;
		if(material.solid()){
			Point point = world.findEmptySpace(x, y);
			blockx = point.x;
			blocky = point.y;
		}
		
		float dist = MoveTowardTask.speed;
		if(world.tiles[x][y].solid() && material.getType().tile()){
			dist = MoveTowardTask.completerange;
		}
		
		if(Vector2.dst(entity.getX(), entity.getY(), blockx * 12 + 6, (blocky) * 12 + 6) > dist){
			insertTask(new MoveTowardTask(blockx, blocky, dist));
			return;
		}else if(world.tiles[x][y].block != Material.air && !entity.group().isBaseBlock(world.tiles[x][y].block, blockx, blocky)&&
				(!material.getType().tile() || world.tiles[x][y].block.getType() == MaterialType.grass  
				|| world.tiles[x][y].block.getType() == MaterialType.tree)){
			insertTask(new BreakBlockTask(world.tiles[x][y].block, x, y));
			return;
		}
		
		if(!waited && material.solid()){
			this.insertTask(new WaitUntilEmptyTask(material, x, y));
			waited = true;
			return;
		}
			
		
		world.tiles[x][y].setMaterial(material);
		world.updateTile(x, y);
		inventory.removeAll(material.getDrops());
		entity.group().registerBlock(entity, material, x, y);
		entity.group().unreserveBlock(x, y);
		finish();
		*/
	}
	
}
