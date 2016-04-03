package net.pixelstatic.koru.renderers;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.ChildComponent;
import net.pixelstatic.koru.components.HitboxComponent;
import net.pixelstatic.koru.components.TextComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.sprites.Layer;
import net.pixelstatic.koru.sprites.Layer.LayerType;

public class IndicatorRenderer extends EntityRenderer{

	@Override
	protected void render(){
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		if(parent == null) return;
		
		child.offset += 0.2f;
		entity.position().set(parent.getX(), parent.getY() + child.offset);
		render.layers.update(entity.getX(), entity.getY(), Layer.posLayer(parent.getY()-1));
	}

	@Override
	protected void initRender(){
		render.layer("").setType(LayerType.TEXT).setText(entity.mapComponent(TextComponent.class).text);
		
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		if(parent == null) return;
		
		child.offset = parent.mapComponent(HitboxComponent.class).height;
	}

}
