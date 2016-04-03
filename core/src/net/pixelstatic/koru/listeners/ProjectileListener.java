package net.pixelstatic.koru.listeners;

import net.pixelstatic.koru.components.ProjectileComponent;
import net.pixelstatic.koru.entities.KoruEntity;

public class ProjectileListener extends CollisionListener{

	@Override
	boolean accept(KoruEntity entity, KoruEntity other){
		return entity.mapComponent(ProjectileComponent.class) != null;
	}

	@Override
	void collision(KoruEntity entity, KoruEntity other){
		entity.removeSelfServer();
	}

}
