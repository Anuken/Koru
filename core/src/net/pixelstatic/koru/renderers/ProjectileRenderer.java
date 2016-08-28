package net.pixelstatic.koru.renderers;

import net.pixelstatic.koru.components.ProjectileComponent;

public class ProjectileRenderer extends EntityRenderer{

	@Override
	protected void render(){
		entity.mapComponent(ProjectileComponent.class).type.draw(entity, render);
	}

	@Override
	protected void initRender(){
		//render.layer("bolt");
	}

}
