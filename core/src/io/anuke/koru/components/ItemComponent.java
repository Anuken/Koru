package io.anuke.koru.components;

import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.systems.KoruEngine;

@Synced
public class ItemComponent implements KoruComponent{
	static Vector2 tmp = new Vector2();
	public ItemStack stack;
	
	@Override
	public void update(KoruEntity entity){
		KoruEngine.instance().map().getNearbyEntities(entity.getX(), entity.getY(), 30, 
		(aentity)-> aentity.hasComponent(InventoryComponent.class) && !aentity.inventory().full(), 
		(other)->{
			float dst =  other.position().dist(entity.position());
					
					if(dst > 2f)
						entity.get(VelocityComponent.class).velocity.add(tmp.set(other.getX() - entity.getX(), other.getY() - entity.getY()).nor().scl(1.5f));
					
			if(dst < 2.5){
				other.get(InventoryComponent.class).addItem(stack);
				other.get(InventoryComponent.class).sendUpdate(other);
				entity.removeServer();
			}
		});
	}
}
