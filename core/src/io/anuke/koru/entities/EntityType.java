package io.anuke.koru.entities;

import io.anuke.koru.components.KoruComponent;


public abstract class EntityType{
	
	public abstract ComponentList components();
	
	public void init(KoruEntity entity){}
	
	public void update(KoruEntity entity){}
	
	public boolean unload(){return true;}
	
	public boolean collide(KoruEntity entity, KoruEntity other){return true;}
	
	public void onDeath(KoruEntity entity, KoruEntity killer){
		entity.removeServer();
	}
	
	protected static ComponentList list(KoruComponent... components){
		return new ComponentList(components);
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName();
	}
}
