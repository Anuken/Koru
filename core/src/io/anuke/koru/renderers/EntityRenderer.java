package io.anuke.koru.renderers;

import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.entities.KoruEntity;

public abstract class EntityRenderer{
	protected boolean init;
	protected KoruEntity entity;
	protected RenderComponent render;
	
	abstract protected void render();
	abstract protected void initRender();
	
	public final void renderInternal(KoruEntity entity, RenderComponent render){
		this.entity = entity;
		this.render = render;
		if(!init){
			initRender();
			init = true;
		}
		this.render();
	}
		
}
