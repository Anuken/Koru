package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.components.InventoryTrait;
import io.anuke.koru.components.ItemTrait;
import io.anuke.koru.entities.KoruEntity;

public class ItemSystem extends KoruSystem{
	private final Vector2 tmp = new Vector2();
	
	public ItemSystem(){
		super(Family.all(ItemTrait.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		getEngine().map().getNearbyEntities(entity.getX(), entity.getY(), 30, 
		(aentity)-> aentity.has(InventoryTrait.class) && !aentity.inventory().full(), 
		(other)->{
			float dst =  other.pos().dist(entity.pos());
			
			//if(dst > 2f)
			//	entity.get(VelocityComponent.class).velocity.add(tmp.set(other.getX() - entity.getX(), other.getY() - entity.getY()).nor().scl(1.5f));
			
			if(dst < 2.5){
				other.get(InventoryTrait.class).addItem(entity.get(ItemTrait.class).stack);
				other.get(InventoryTrait.class).sendUpdate(other);
				entity.removeServer();
			}
		});
	}
}
