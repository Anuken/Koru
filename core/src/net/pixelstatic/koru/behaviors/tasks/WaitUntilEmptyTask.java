package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.components.HitboxComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.MaterialType;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;

public class WaitUntilEmptyTask extends Task{
	@SuppressWarnings("unchecked")
	private Family family = Family.all(HitboxComponent.class).get();
	private Rectangle rect = new Rectangle();
	public final Material material;
	public final int x,y;
	
	public WaitUntilEmptyTask(Material material, int x, int y){
		this.x = x;
		this.y = y;
		this.material = material;
	}
	
	@Override
	protected void update(){
		ImmutableArray<Entity> entities = KoruUpdater.instance.engine.getEntitiesFor(family);
		MaterialType.block.getRect(x, y, rect);
		rect.x -= 2;
		rect.y -=2;
		rect.width += 4;
		rect.height += 4;
		for(Entity e : entities){
			KoruEntity entity = (KoruEntity)e;
			
			if(entity.mapComponent(HitboxComponent.class).terrainhitbox.collides(rect)){
				return;
			}
		}
		finish();
	}
	
}
