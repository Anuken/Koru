package io.anuke.koru.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.IServer;

public class KoruEngine extends Engine{
	private EntityMapper map;
	//public QuadTree<KoruEntity> quadtree = new QuadTree<KoruEntity>(5, new Rectangle());
	
	public KoruEngine(){
		super();
		
		KoruEntity.setEngine(this);
		map = new EntityMapper();
		
		addEntityListener(map);
		addSystem(new VelocitySystem());
		addSystem(new FadeSystem());
		if(IServer.active()) addSystem(map);
	}
	
	public EntityMapper map(){
		return map;
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
		if(entity == null) throw new RuntimeException("The entity cannot be null!");
		if(!(entity instanceof KoruEntity)) throw new RuntimeException("Only KoruEntities can be added to the engine!");
		super.addEntity(entity);
	}
	
}
