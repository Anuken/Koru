package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.behaviors.Behavior;
import net.pixelstatic.koru.components.BehaviorComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Family;

public class BehaviorSystem extends KoruSystem{
	
	@SuppressWarnings("unchecked")
	public BehaviorSystem(){
		super(Family.all(PositionComponent.class, BehaviorComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		BehaviorComponent component = entity.mapComponent(BehaviorComponent.class);
		for(Behavior behavior : component.behaviorarray){
			behavior.internalUpdate(entity, (KoruEngine)this.getEngine());
			if(behavior.isBlocking()) break;
		}
	}

}
