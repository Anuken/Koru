package io.anuke.koru.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.entities.KoruEntity;

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
		//don't remove local player
		if(render != null && !(entity.getComponent(ConnectionComponent.class) != null && entity.getComponent(ConnectionComponent.class).local == true)) render.models.free();

		entities.remove(((KoruEntity)entity).getID());

	}
}
