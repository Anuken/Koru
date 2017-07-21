package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.traits.InputTrait;

public class InputSystem extends KoruSystem{

	public InputSystem() {
		super(Family.all(InputTrait.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		entity.get(InputTrait.class).input.update(delta);
	}

}
