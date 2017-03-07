package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.Renderer;

public class RendererSystem extends KoruSystem{
	public static boolean renderHitboxes = false;
	Renderer renderer;

	public RendererSystem(Renderer renderer){
		super(Family.all(PositionComponent.class, RenderComponent.class).get(), 10);
		this.renderer = renderer;
	}

	@Override
	protected void processEntity(KoruEntity entity, float deltaTime){
		RenderComponent render = entity.renderer();
		
		render.renderer.renderInternal(entity, render);
	}
}
