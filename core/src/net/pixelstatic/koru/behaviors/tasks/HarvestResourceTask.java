package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.Tile;

import com.badlogic.gdx.math.Vector2;

public class HarvestResourceTask extends Task{
	private int searchrange = 50;
	private Item item;

	public HarvestResourceTask(Item item){
		this.item = item;
	}

	@Override
	protected void update(){
		World world = KoruUpdater.instance.world;
		int ex = World.tile(entity.position().x);
		int ey = World.tile(entity.position().y);
		int nearestx = -9999, nearesty = -9999;
		float closest = Float.MAX_VALUE;
		Material selected = null;
		for(int x = -searchrange;x <= searchrange;x ++){
			for(int y = -searchrange;y <= searchrange;y ++){
				int worldx = ex + x, worldy = ey + y;
				if( !World.inBounds(worldx, worldy)) continue;
				Tile tile = world.tiles[worldx][worldy];
				Material material = tile.block;

				if(tile.block.breakable() && tile.block.dropsItem(item)){
					material = tile.block;
				}else if(tile.tile.breakable() && tile.tile.dropsItem(item)){
					material = tile.tile;
				}else{
					continue;
				}

				float dist = Vector2.dst(0, 0, x, y);
				if(dist < closest && !Group.instance.blockReserved(worldx, worldy)){
					nearestx = x;
					nearesty = y;
					closest = dist;
					selected = material;
				}
			}
		}
		if(nearestx == -9999 || nearesty == -9999 || selected == null){
			finish();
			return;
		}
		int targetx = ex + nearestx;
		int targety = ey + nearesty;
		Group.instance.reserveBlock(targetx, targety);
		this.insertTask(new BreakBlockTask(selected, targetx, targety));
		this.insertTask(new MoveTowardTask(targetx * 12 + 6, targety * 12 + 6));
	}
}
