package net.pixelstatic.koru.renderers;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.ChildComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.sprites.Layer.LayerType;

public class IndicatorRenderer extends EntityRenderer{

	@Override
	protected void render(){
		KoruEntity parent = Koru.getEngine().getEntity(entity.mapComponent(ChildComponent.class).parent);
		if(parent == null) return;
		
		entity.position().set(parent.getX(), parent.getY());
		render.layers.update(entity.getX(), entity.getY());
	//	Koru.log(render.layers.layer("").x);
	}

	@Override
	protected void initRender(){
		render.layer("").setType(LayerType.TEXT).setText("hit!");
	}

}
