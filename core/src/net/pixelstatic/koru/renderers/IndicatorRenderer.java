package net.pixelstatic.koru.renderers;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.*;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.sprites.Layer.LayerType;

public class IndicatorRenderer extends EntityRenderer{

	@Override
	protected void render(){
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		
		child.offset += 0.2f * 20f/entity.mapComponent(FadeComponent.class).lifetime;
		if(parent != null)entity.position().set(parent.getX(), parent.getY() + child.offset);
		//TODO poslayer
		render.layers.update(entity.getX(), entity.getY(),parent != null ?  100 : entity.getY() + 25);
	}

	@Override
	protected void initRender(){
		render.layer("").setType(LayerType.TEXT).setColor(entity.mapComponent(TextComponent.class).color).setText(entity.mapComponent(TextComponent.class).text);
		
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		if(parent == null) return;
		
		child.offset = parent.mapComponent(HitboxComponent.class).height;
	}

}
