package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.RenderComponent;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.utils.ObjectMap;

public class MapListener implements EntityListener{
	public ObjectMap<Long, KoruEntity> entities = new ObjectMap<Long, KoruEntity>();
	//public

	@Override
	public void entityAdded(Entity entity){
		entities.put(((KoruEntity)entity).getID(), (KoruEntity)entity);
	}

	@Override
	public void entityRemoved(Entity entity){
		RenderComponent render = entity.getComponent(RenderComponent.class);
		if(render != null) render.group.free();

		entities.remove(((KoruEntity)entity).getID());

	}

}
