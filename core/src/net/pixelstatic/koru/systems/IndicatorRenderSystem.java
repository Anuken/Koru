package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.ChildComponent;
import net.pixelstatic.koru.components.RenderComponent;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Family;

public class IndicatorRenderSystem extends KoruSystem{

	@SuppressWarnings("unchecked")
	public IndicatorRenderSystem(){
		super(Family.all(ChildComponent.class, RenderComponent.class).get(), 0);
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		KoruEntity parent = ((KoruEngine)getEngine()).getEntity(entity.mapComponent(ChildComponent.class).parent);
		if(parent == null) return;

		entity.position().set(parent.getX(), parent.getY());

	}

}
