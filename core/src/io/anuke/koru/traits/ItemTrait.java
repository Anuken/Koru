package io.anuke.koru.traits;

import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;

@Synced
public class ItemTrait extends Trait{
	public ItemStack stack;
	
	@Override
	public void update(Spark spark){
		//TODO
		/*
		KoruEngine.instance().map().getNearbyEntities(entity.getX(), entity.getY(), 30, 
		(aentity)-> aentity.has(InventoryTrait.class) && !aentity.inventory().full(), 
		(other)->{
			float dst =  other.pos().dist(entity.pos());
					
					if(dst > 2f)
						entity.get(VelocityComponent.class).velocity.add(tmp.set(other.getX() - entity.getX(), other.getY() - entity.getY()).nor().scl(1.5f));
					
			if(dst < 1.5){
				other.get(InventoryTrait.class).addItem(stack);
				other.get(InventoryTrait.class).sendUpdate(other);
				entity.removeServer();
			}
		});
		*/
	}
}
