package io.anuke.koru.systems;

import io.anuke.koru.components.FadeComponent;
import io.anuke.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class FadeSystem extends IteratingSystem{

	public FadeSystem(){
		super(Family.all(FadeComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float delta){
		KoruEntity koru = (KoruEntity)entity;
		FadeComponent fade = koru.mapComponent(FadeComponent.class);
		fade.life += delta;
		if(fade.life > fade.lifetime){
			koru.removeSelf();
		}
	}

}
