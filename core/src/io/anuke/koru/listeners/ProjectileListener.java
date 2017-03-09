package io.anuke.koru.listeners;

import io.anuke.koru.components.ProjectileComponent;
import io.anuke.koru.entities.KoruEntity;

public class ProjectileListener extends CollisionListener{

	@Override
	boolean accept(KoruEntity entity, KoruEntity other){
		return entity.has(ProjectileComponent.class);
	}

	@Override
	void contact(KoruEntity entity, KoruEntity other){
		if(!entity.get(ProjectileComponent.class).type.pierce()){
			entity.removeServer();
		}else{
			entity.get(ProjectileComponent.class).hit.add(other.getID());
		}
	}

}
