package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.HitboxComponent;
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
		RenderComponent render = entity.mapComponent(RenderComponent.class);
		
		render.renderer.renderInternal(entity, render);
		if(renderHitboxes) renderHitboxes(entity, render);
	}

	public void renderHitboxes(KoruEntity entity, RenderComponent render){
		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		if(hitbox != null){
			hitbox.terrainhitbox.update(entity);
			hitbox.entityhitbox.update(entity);
			//TODO actually render the hitboxes?
		}
	}
}
