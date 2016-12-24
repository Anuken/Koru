package io.anuke.koru.renderers;

import io.anuke.koru.components.ProjectileComponent;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.spritesystem.SortProviders;
import io.anuke.ucore.spritesystem.SpriteRenderable;

public class ProjectileRenderer extends EntityRenderer{

	@Override
	protected void render(){
		entity.mapComponent(ProjectileComponent.class).type.draw(entity, render);
	}

	@Override
	protected void initRender(){
		SpriteRenderable sprite = new SpriteRenderable(Resources.region(entity.get(ProjectileComponent.class).type.name()));//.add("bolt", render.group);
		sprite.sprite.setOriginCenter();
		//TODO specify shadows or not?
		//sprite.addShadow(render.group, Resources.atlas());
		sprite.setProvider(SortProviders.object);
		sprite.add("bolt", render.group);
	}

}
