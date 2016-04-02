package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.*;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.Renderer;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.sprites.Layer;

import com.badlogic.ashley.core.Family;

public class RendererSystem extends KoruSystem{
	public static boolean renderHitboxes = false;
	Renderer renderer;

	@SuppressWarnings("unchecked")
	public RendererSystem(Renderer renderer){
		super(Family.all(PositionComponent.class, RenderComponent.class)/*.exclude(ProjectileComponent.class, ConnectionComponent.class)*/.get(), 10);
		this.renderer = renderer;
	}

	@Override
	protected void processEntity(KoruEntity entity, float deltaTime){
		limitPosition(entity);
		RenderComponent render = entity.mapComponent(RenderComponent.class);
		
		render.renderer.renderInternal(entity, render);
		//entity.getType().render(entity, render);

		addFade(entity, render);

		if(renderHitboxes) renderHitboxes(entity, render);
	}

	void addFade(KoruEntity entity, RenderComponent render){
		FadeComponent fade = entity.mapComponent(FadeComponent.class);
		for(Layer layer : render.layers.values()){
			layer.update(entity.getX(), entity.getY());
			if(fade != null && fade.render) layer.color.a = 1f - fade.life / fade.lifetime;
		}
	}

	public void renderHitboxes(KoruEntity entity, RenderComponent render){
		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		if(hitbox != null){
			hitbox.terrainhitbox.update(entity);
			renderer.layer("hitbox", hitbox.terrainRect().x, hitbox.terrainRect().y).setShape(hitbox.terrainRect().width, hitbox.terrainRect().height).setLayer(999999999);

			hitbox.entityhitbox.update(entity);
			renderer.layer("hitbox2", hitbox.entityRect().x, hitbox.entityRect().y).setShape(hitbox.entityRect().width, hitbox.entityRect().height).setLayer(99999999);
		}
	}

	void limitPosition(KoruEntity player){
		if(player.getX() < 0) player.position().x = 0;
		if(player.getY() < 0) player.position().y = 0;
		if(player.getX() > World.worldWidthPixels()) player.position().x = World.worldWidthPixels();
		if(player.getY() > World.worldHeightPixels()) player.position().y = World.worldHeightPixels();

	}
}
