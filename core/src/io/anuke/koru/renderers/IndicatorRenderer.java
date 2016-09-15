package io.anuke.koru.renderers;

import io.anuke.koru.Koru;
import io.anuke.koru.components.*;
import io.anuke.koru.entities.KoruEntity;
import net.pixelstatic.gdxutils.spritesystem.TextRenderable;

public class IndicatorRenderer extends EntityRenderer{

	@Override
	protected void render(){
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		
		child.offset += 0.2f * 20f/entity.mapComponent(FadeComponent.class).lifetime;
		if(parent != null)entity.position().set(parent.getX(), parent.getY() + child.offset);
		
		render.group.setPosition(entity.getX(), entity.getY());
		//render.layers.update(entity.getX(), entity.getY(), parent != null ?  100 : entity.getY() + 25);
	}

	@Override
	protected void initRender(){
	//	render.layer("").setType(LayerType.TEXT).setColor(entity.mapComponent(TextComponent.class).color).setText(entity.mapComponent(TextComponent.class).text);
		render.group.add("text", new TextRenderable(null, entity.mapComponent(TextComponent.class).text));
		
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		if(parent == null) return;
		
		child.offset = parent.mapComponent(HitboxComponent.class).height;
	}

}
