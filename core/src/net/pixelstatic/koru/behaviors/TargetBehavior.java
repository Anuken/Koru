package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.components.HealthComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.server.KoruUpdater;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

@SuppressWarnings("unchecked")
public class TargetBehavior extends Behavior{
	static Family family = Family.all(HealthComponent.class).get();
	static final int targetTime = 10;
	public float targetrange = 100f;
	public EntityType targetType;
	public KoruEntity target;
	
	@Override
	protected void update(){
		if(KoruUpdater.frameID() % targetTime != 0) return;
		ImmutableArray<Entity> entities = engine.getEntitiesFor(family);
		KoruEntity closest = null;
		float mindist = Float.MAX_VALUE;
		for(Entity anentity : entities){
			if(anentity == entity) continue;
			KoruEntity entity = (KoruEntity)anentity;
			if(entity.getType() == targetType){
				float dst = entity.position().dist(this.entity.position());
				if(dst < mindist && dst < targetrange){
					dst = mindist;
					closest = entity;
				}
			}
		}
		
		target = closest;	
	}
	
	public TargetBehavior setRange(float range){
		targetrange = range;
		return this;
	}
	
	public TargetBehavior setType(EntityType type){
		targetType = type;
		return this;
	}
}
