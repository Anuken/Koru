package net.pixelstatic.koru.listeners;

import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.gdx.utils.Array;

public class CollisionHandler{
	private Array<CollisionListener> listeners = new Array<CollisionListener>();
	
	public CollisionHandler(){
		addListener(new DamageListener());
	}
	
	public void dispatchEvent(KoruEntity entity, KoruEntity other){
		dispatch(entity, other);
		dispatch(other, entity);
	}
	
	private void dispatch(KoruEntity entity, KoruEntity other){
		for(CollisionListener listener : listeners){
			if(listener.accept(entity, other)) listener.collision(entity, other);
		}
	}
	
	public void addListener(CollisionListener listener){
		listeners.add(listener);
	}
}
