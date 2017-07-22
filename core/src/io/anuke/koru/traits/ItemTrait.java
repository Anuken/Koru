package io.anuke.koru.traits;

import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.systems.EntityMapper;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.ecs.extend.traits.VelocityTrait;
import io.anuke.ucore.util.Tmp;

@Synced
public class ItemTrait extends Trait{
	public ItemStack stack;
	
	@Override
	public void update(Spark spark){
		if(!IServer.active()) return;
		
		IServer.instance().getBasis().getProcessor(EntityMapper.class).getNearbyEntities(spark.pos().x, spark.pos().y, 30, 
		aentity-> aentity.has(InventoryTrait.class) && !aentity.get(InventoryTrait.class).full(), 
		(other)->{
			float dst =  other.pos().dst(spark.pos());
					
			if(dst > 2f)
				spark.get(VelocityTrait.class).vector.add(Tmp.v1.set(other.pos().x - spark.pos().x, 
						other.pos().y - spark.pos().y).nor().scl(1.5f));
					
			if(dst < 1.5){
				other.get(InventoryTrait.class).addItem(stack);
				other.get(InventoryTrait.class).sendUpdate(other);
				IServer.instance().removeSpark(spark);
			}
		});
		
	}
}
