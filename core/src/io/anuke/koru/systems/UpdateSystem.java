package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.KoruComponent;
import io.anuke.koru.entities.KoruEntity;

public class UpdateSystem extends KoruSystem{

	public UpdateSystem() {
		super(Family.exclude().get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		for(int i = 0; i < entity.getComponents().size(); i ++){
			((KoruComponent)entity.getComponents().get(i)).update(entity);
		}
	}

}
