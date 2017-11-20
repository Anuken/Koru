package io.anuke.koru.traits;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.network.Net;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.Require;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.ecs.extend.traits.LifetimeTrait;

@Synced
@Require({LifetimeTrait.class})
public class EffectTrait extends Trait{
	public int id;
	public float rotation;
	public Color color = Color.WHITE;
	
	@Override
	public void added(Spark spark){
		if(Net.server()){
			throw new IllegalArgumentException("Effect entities should not be added serverside!");
		}
		
		spark.get(LifetimeTrait.class).lifetime = Effects.getEffect(id).lifetime;
	}
}
