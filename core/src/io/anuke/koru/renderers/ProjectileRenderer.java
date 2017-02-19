package io.anuke.koru.renderers;

import io.anuke.koru.components.ProjectileComponent;

public class ProjectileRenderer extends EntityRenderer{

	@Override
	protected void render(){
	}

	@Override
	protected void init(){
		entity.get(ProjectileComponent.class).type.draw(entity, this, entity.get(ProjectileComponent.class));
	}

}
