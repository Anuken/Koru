package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.components.VelocityComponent;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;

import com.badlogic.gdx.math.Vector2;

public class PlaceBlockBehavior extends Behavior{
	int x, y;
	Material material;
	boolean placed;
	
	public PlaceBlockBehavior(Material mat, int x, int y){
		material = mat;
		this.x = x;
		this.y = y;
		blocking = true;
	}

	@Override
	protected void update(){
		if(!World.inBounds(x, y)) return;
		float tx = x*12+6, ty = y*12+6;
		entity.mapComponent(VelocityComponent.class).velocity.set(tx - entity.getX(), ty - entity.getY())
		.setLength(0.5f);
		
		if(placed){
			World world = KoruUpdater.instance.world;
			world.tiles[x][y].block = material;
			world.updateTile(x, y);
			removeSelf();
			return;
		}
		
		if(Vector2.dst(tx, ty, entity.position().x, entity.position().y) < 10){
			placed = true;
			return;
		}
	}
}
