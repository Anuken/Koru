package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.FadeComponent;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class FadeSystem extends IteratingSystem{

	@SuppressWarnings("unchecked")
	public FadeSystem(){
		super(Family.all(FadeComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float delta){
		KoruEntity koru = (KoruEntity)entity;
		FadeComponent fade = koru.mapComponent(FadeComponent.class);
		//Koru.log(fade.lifetime);
		fade.life += delta;
		if(fade.life > fade.lifetime){
			koru.removeSelf();
		}
	}

}
