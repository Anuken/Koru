package io.anuke.koru.traits;

import io.anuke.koru.Koru;
import io.anuke.koru.graphics.Fx;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.Net;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.systems.EntityMapper;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.ecs.extend.traits.VelocityTrait;
import io.anuke.ucore.util.Tmp;

@Synced
public class ItemTrait extends Trait{
	public ItemStack stack;
	
	@Override
	public void update(Spark spark){
		if(!Net.server()) return;
		
		Koru.basis.getProcessor(EntityMapper.class).getNearbyEntities(spark.pos().x, spark.pos().y, 30, 
		aentity-> aentity.has(InventoryTrait.class) && !aentity.get(InventoryTrait.class).full(), 
		(other)->{
			float height = 3f;
			float dst =  other.pos().dst(spark.pos().x, spark.pos().y - height);
					
			if(dst > 2f){
				spark.get(VelocityTrait.class).vector.add(Tmp.v1.set(other.pos().x - spark.pos().x, 
						other.pos().y - (spark.pos().y - height)).nor().scl(0.2f * Timers.delta()));
			}
					
			if(dst < 2f){
				other.get(InventoryTrait.class).addItem(stack);
				other.get(InventoryTrait.class).sendUpdate(other);
				Net.removeSpark(spark);
				Effects.effect(Fx.itempickup, spark.pos().x, spark.pos().y - 0.5f);
			}
		});
		
	}
}
