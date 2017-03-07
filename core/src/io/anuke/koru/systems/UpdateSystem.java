package io.anuke.koru.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.KoruComponent;
import io.anuke.koru.entities.KoruEntity;

public class UpdateSystem extends KoruSystem{

	public UpdateSystem() {
		super(Family.all(KoruComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		for(Component c : entity.getComponents()){
			((KoruComponent)c).update(entity);
		}
	}

}
