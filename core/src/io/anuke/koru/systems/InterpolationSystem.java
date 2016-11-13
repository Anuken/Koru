package io.anuke.koru.systems;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.SyncComponent;
import io.anuke.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Family;

public class InterpolationSystem extends KoruSystem{

	public InterpolationSystem(){
		super(Family.all(SyncComponent.class, PositionComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		if(entity.mapComponent(ConnectionComponent.class) != null && entity.mapComponent(ConnectionComponent.class).local == true) return;
		SyncComponent sync = entity.mapComponent(SyncComponent.class);

		if(sync.interpolator != null) sync.interpolator.update(entity);
	}

}
