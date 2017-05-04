package io.anuke.koru.components;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.IServer;
import io.anuke.koru.renderers.EntityRenderer;
import io.anuke.ucore.renderables.RenderableList;

public class RenderComponent implements KoruComponent{
	public final EntityRenderer renderer;
	public RenderableList list = new RenderableList();
	public int direction;
	public float walkframe = 0;
	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
	
	@Override
	public void update(KoruEntity entity){
		if(!IServer.active())
		renderer.renderInternal(entity, this);
	}
	
	@Override
	public void onRemove(KoruEntity entity){
		if(!(entity.connection() != null && entity.connection().local == true))
		list.free();
	}
}
