package io.anuke.koru.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.IServer;

public class KoruEngine extends Engine{
	private EntityMapper map;
	private Object lock = new Object();

	public KoruEngine() {
		super();

		KoruEntity.setEngine(this);
		map = new EntityMapper();

		addEntityListener(map);
		addSystem(new VelocitySystem());
		addSystem(new FadeSystem());
		addSystem(new ItemSystem());
		
		if(IServer.active())
			addSystem(map);
	}

	public EntityMapper map(){
		return map;
	}

	public KoruEntity getEntity(long id){
		return map.entities.get(id);
	}

	public boolean removeEntity(long id){
		if(!map.entities.containsKey(id))
			return false;
		map.entities.get(id).removeSelf();
		return true;
	}

	@Override
	public void update(float deltaTime){
		synchronized(lock){
			super.update(deltaTime);
		}
	}
	
	@Override
	public void removeEntity(Entity entity){
		synchronized(lock){
			super.removeEntity(entity);
		}
	}

	@Override
	public void addEntity(Entity entity){
		synchronized(lock){
			if(entity == null)
				throw new RuntimeException("The entity cannot be null!");
			if(!(entity instanceof KoruEntity))
				throw new RuntimeException("Only KoruEntities can be added to the engine!");
			//we have an entity conflict. ignore.
			if(map().entities.containsKey(((KoruEntity)entity).getID())) throw new RuntimeException("Entity conflict! An entity with that ID already exists!");
			
			super.addEntity(entity);
		}
	}

}
