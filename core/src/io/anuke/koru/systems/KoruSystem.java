package io.anuke.koru.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.anuke.koru.entities.KoruEntity;

public abstract class KoruSystem extends IteratingSystem{

	public KoruSystem(Family family){
		super(family);
	}
	
	public KoruSystem(Family family, int priority){
		super(family, priority);
	}

	@Override
	protected final void processEntity(Entity entity, float deltaTime){
		processEntity((KoruEntity)entity, deltaTime);
	}
	
	abstract void processEntity(KoruEntity entity, float delta);
	
	@Override
	public KoruEngine getEngine () {
		return (KoruEngine)super.getEngine();
	}
}
