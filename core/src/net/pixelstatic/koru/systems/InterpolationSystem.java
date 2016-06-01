package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.ConnectionComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.components.SyncComponent;
import net.pixelstatic.koru.entities.KoruEntity;

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
