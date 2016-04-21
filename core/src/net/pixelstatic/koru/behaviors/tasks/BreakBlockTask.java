package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;

import com.badlogic.gdx.math.Vector2;

public class BreakBlockTask extends Task{
	static final int speed = 1;
	int blockx, blocky;
	Material material;
	boolean waited = false;
	
	public BreakBlockTask(Material material, int x, int y){
		blockx = x;
		blocky = y;
		this.material = material;
		if(this.material == null) this.material = World.instance().tiles[blockx][blocky].block;
	}
	
	@Override
	protected void update(){
		if(!World.inBounds(blockx, blocky)){ 
			finish();
			return;
		}
		if(Vector2.dst(entity.getX(), entity.getY(), blockx * 12 + 6, (blocky) * 12 + 6) > MoveTowardTask.completerange){
			this.insertTask(new MoveTowardTask(blockx, blocky ));
			return;
		}
		if(!waited){
			this.insertTask(new WaitTask(speed));
			waited = true;
			return;
		}
		World world = KoruUpdater.instance.world;
		
		material.harvestEvent(world.tiles[blockx][blocky]);
		world.updateTile(blockx, blocky);
		entity.mapComponent(InventoryComponent.class).addItems(material.getDrops());
		entity.group().unreserveBlock(blockx, blocky);
		finish();
	}
}
