package io.anuke.koru.renderers;

import com.badlogic.gdx.utils.Align;

import io.anuke.koru.Koru;
import io.anuke.koru.components.ChildComponent;
import io.anuke.koru.components.FadeComponent;
import io.anuke.koru.components.HitboxComponent;
import io.anuke.koru.components.TextComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.spritesystem.Sorter;
import io.anuke.ucore.spritesystem.TextRenderable;

public class IndicatorRenderer extends EntityRenderer{
	float lastx, lasty;
	
	@Override
	protected void render(){
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		
		child.offset += 0.2f * 20f/entity.mapComponent(FadeComponent.class).lifetime;
		
		if(parent != null){
			entity.position().set(parent.getX(), parent.getY() + child.offset);
		}
		
		render.group.setPosition(entity.getX(), entity.getY());
	}

	@Override
	protected void initRender(){
	
		new TextRenderable(Resources.font(), entity.mapComponent(TextComponent.class).text)
		.align(Align.right)
		.setProvider(Sorter.object)
		.add("text", render.group);
		
		ChildComponent child = entity.mapComponent(ChildComponent.class);
		KoruEntity parent = Koru.getEngine().getEntity(child.parent);
		if(parent == null) return;
		
		child.offset = parent.mapComponent(HitboxComponent.class).height;
	}

}
