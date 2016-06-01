package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.behaviors.Behavior;
import net.pixelstatic.koru.components.BehaviorComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Family;

public class BehaviorSystem extends KoruSystem{
	
	public BehaviorSystem(){
		super(Family.all(PositionComponent.class, BehaviorComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		BehaviorComponent component = entity.mapComponent(BehaviorComponent.class);
		Object[] behaviors = component.behaviorarray.begin();
		for(Object object : behaviors ){
			Behavior behavior = (Behavior)object;
			if(behavior == null) continue;
			behavior.internalUpdate(entity, (KoruEngine)this.getEngine());
			if(behavior.isBlocking()) break;
		}
		component.behaviorarray.end();
	}

}
