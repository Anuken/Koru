package io.anuke.koru.entities.types;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.entities.Prototypes;
import io.anuke.koru.traits.EffectTrait;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.traits.LifetimeTrait;
import io.anuke.ucore.ecs.extend.traits.PosTrait;
import io.anuke.ucore.ecs.extend.traits.RenderableTrait;

public class Effect extends Prototype{

	@Override
	public TraitList traits(){
		return new TraitList(
			new PosTrait(),
			new EffectTrait(),
			new LifetimeTrait(),
			new RenderableTrait((trait, spark)->{
				trait.draw(d->{
					d.layer = spark.pos().y;
					
					EffectTrait effect = spark.get(EffectTrait.class);
					Effects.renderEffect(Effects.getEffect(effect.name), effect.color, spark.life().life, spark.pos().x, spark.pos().y);
				});
			})
		);
	}
	
	public static Spark create(String name, Color color, float x, float y){
		Spark spark = new Spark(Prototypes.effect);
		spark.get(EffectTrait.class).name = name;
		spark.get(EffectTrait.class).color = color;
		spark.pos().set(x, y);
		return spark;
	}
}
