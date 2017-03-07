package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.SyncComponent;
import io.anuke.koru.entities.KoruEntity;

public class InterpolationSystem extends KoruSystem{

	public InterpolationSystem(){
		super(Family.all(SyncComponent.class, PositionComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		if(entity.connection() != null && entity.connection().local == true) return;
		SyncComponent sync = entity.get(SyncComponent.class);

		if(sync.interpolator != null) sync.interpolator.update(entity);
	}

}
