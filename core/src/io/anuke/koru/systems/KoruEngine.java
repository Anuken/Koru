package io.anuke.koru.systems;

import io.anuke.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class KoruEngine extends Engine{
	private MapListener map;
	
	public KoruEngine(){
		super();
		KoruEntity.setEngine(this);
		map = new MapListener();
		this.addEntityListener(map);
		this.addSystem(new VelocitySystem());
		this.addSystem(new FadeSystem());
	}
	
	public KoruEntity getEntity(long id){
		return map.entities.get(id);
	}
	
	public boolean removeEntity(long id){
		if(!map.entities.containsKey(id))return false;
		map.entities.get(id).removeSelf();
		return true;
	}
	
	@Override
	public void addEntity(Entity entity){
		if(entity == null) throw new RuntimeException("KoruEntity cannot be null!");
		if(!(entity instanceof KoruEntity)) throw new RuntimeException("Only KoruEntities can be added to the engine!");
		super.addEntity(entity);
	}
	
}
