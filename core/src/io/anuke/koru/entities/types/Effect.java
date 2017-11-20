package io.anuke.koru.entities.types;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.entities.Prototypes;
import io.anuke.koru.traits.EffectTrait;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.traits.FacetTrait;
import io.anuke.ucore.ecs.extend.traits.LifetimeTrait;
import io.anuke.ucore.ecs.extend.traits.PosTrait;

public class Effect extends Prototype{

	@Override
	public TraitList traits(){
		return new TraitList(
			new PosTrait(),
			new EffectTrait(),
			new LifetimeTrait(),
			new FacetTrait((trait, spark)->{
				trait.draw(d->{
					d.layer = spark.pos().y;
					
					EffectTrait effect = spark.get(EffectTrait.class);
					Effects.renderEffect(spark.getID(), Effects.getEffect(effect.id), 
							effect.color, spark.life().life, spark.pos().x, spark.pos().y, effect.rotation);
				});
			})
		);
	}
	
	public static Spark create(io.anuke.ucore.core.Effects.Effect effect, Color color, float x, float y){
		return create(effect, color, x, y, 0);
	}
	
	public static Spark create(io.anuke.ucore.core.Effects.Effect effect, Color color, float x, float y, float rotation){
		Spark spark = new Spark(Prototypes.effect);
		spark.get(EffectTrait.class).id = effect.id;
		spark.get(EffectTrait.class).color = color;
		spark.pos().set(x, y);
		return spark;
	}
}
