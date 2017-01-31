package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.components.ItemComponent;
import io.anuke.koru.components.VelocityComponent;
import io.anuke.koru.entities.KoruEntity;

public class ItemSystem extends KoruSystem{
	private final Vector2 tmp = new Vector2();
	
	public ItemSystem(){
		super(Family.all(ItemComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		getEngine().map().getNearbyEntities(entity.getX(), entity.getY(), 30, 
		(aentity)-> aentity.hasComponent(InventoryComponent.class) && !aentity.get(InventoryComponent.class).full(), 
		(other)->{
			float dst =  other.position().dist(entity.position());
			
			if(dst > 2f)
				entity.get(VelocityComponent.class).velocity.add(tmp.set(other.getX() - entity.getX(), other.getY() - entity.getY()).nor().scl(1.5f));
			
			if(dst < 2.5){
				other.get(InventoryComponent.class).addItem(entity.get(ItemComponent.class).stack);
				other.get(InventoryComponent.class).sendUpdate(other);
				entity.removeServer();
			}
		});
	}
}
