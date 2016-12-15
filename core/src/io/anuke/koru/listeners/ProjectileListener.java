package io.anuke.koru.listeners;

import io.anuke.koru.components.ProjectileComponent;
import io.anuke.koru.entities.KoruEntity;

public class ProjectileListener extends CollisionListener{

	@Override
	boolean accept(KoruEntity entity, KoruEntity other){
		return entity.hasComponent(ProjectileComponent.class);
	}

	@Override
	void contact(KoruEntity entity, KoruEntity other){
		entity.removeSelfServer();
	}

}
