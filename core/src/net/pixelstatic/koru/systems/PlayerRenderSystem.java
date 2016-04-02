package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.ConnectionComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.components.RenderComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.sprites.Layer.LayerType;

import com.badlogic.ashley.core.Family;

public class PlayerRenderSystem extends KoruSystem{

	@SuppressWarnings("unchecked")
	public PlayerRenderSystem(){
		super(Family.all(ConnectionComponent.class, PositionComponent.class, RenderComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		entity.mapComponent(RenderComponent.class).layers.update(entity.getX(), entity.getY());
		if(!entity.mapComponent(ConnectionComponent.class).local)entity.mapComponent(RenderComponent.class).layers.layer("name", "").setText(entity.mapComponent(ConnectionComponent.class).name)
		.setScale(1f).setPosition(entity.getX(), entity.getY() + 2f).yLayer(entity.getY()-4f).setType(LayerType.TEXT);
	}

}
