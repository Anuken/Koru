package net.pixelstatic.koru.systems;

import java.util.HashMap;

import net.pixelstatic.koru.components.RenderComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.sprites.Layer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;

public class MapListener implements EntityListener{
	public HashMap<Long, KoruEntity> entities = new HashMap<Long, KoruEntity>();

	@Override
	public void entityAdded(Entity entity){
		entities.put(((KoruEntity)entity).getID(), (KoruEntity)entity);
	}

	@Override
	public void entityRemoved(Entity entity){
		RenderComponent render = entity.getComponent(RenderComponent.class);
		if(render != null) for(Layer layer : render.layers.values())
			layer.free();

		entities.remove(((KoruEntity)entity).getID());

	}

}
