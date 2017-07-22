package io.anuke.koru.entities.types;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.koru.entities.Prototypes;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.SyncType;
import io.anuke.koru.traits.ItemTrait;
import io.anuke.koru.traits.SyncTrait;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.traits.PosTrait;
import io.anuke.ucore.ecs.extend.traits.RenderableTrait;
import io.anuke.ucore.ecs.extend.traits.VelocityTrait;

public class ItemDrop extends Prototype{

	@Override
	public TraitList traits(){
		return new TraitList(
			new PosTrait(), 
			new RenderableTrait((trait, spark)->{
				String itemname = spark.get(ItemTrait.class).stack.item.name();
				String region = Draw.hasRegion(itemname + "chunk") ? itemname + "chunk" : itemname + "item";
				
				trait.draw(p->{
					p.layer = spark.pos().y;
					
					Draw.grect(region, spark.pos().x, spark.pos().y-2);
				});
				
				trait.drawShadow(spark, 6, 0);
			}),
			new SyncTrait(SyncType.position),
			new ItemTrait(), 
			new VelocityTrait()
		);
	}
	
	public static void create(float x, float y, ItemStack[] stacks){
		int stepsize = 5;
		float range = 5;
		
		for(ItemStack stack : stacks){
			
			int amount = stack.amount;
			while(amount >= 0){
				Spark item = new Spark(Prototypes.itemDrop);
				item.get(ItemTrait.class).stack = new ItemStack(stack.item, amount > stepsize ? stepsize : amount);
				item.pos().set(x + MathUtils.random(-range,range), y + MathUtils.random(-range,range));
				amount -= stepsize;
				
				IServer.instance().sendSpark(item.add());
			}
		}
		
	}
}
