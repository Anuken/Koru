package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.components.ProjectileComponent;
import net.pixelstatic.koru.components.RenderComponent;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class ProjectileRenderSystem extends IteratingSystem{

	@SuppressWarnings("unchecked")
	public ProjectileRenderSystem(){
		super(Family.all(ProjectileComponent.class, RenderComponent.class, PositionComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime){
		KoruEntity koru = (KoruEntity)entity;
		koru.mapComponent(ProjectileComponent.class).type.draw(koru, koru.mapComponent(RenderComponent.class));
	}

}
