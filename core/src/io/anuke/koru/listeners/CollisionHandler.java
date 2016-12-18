package io.anuke.koru.listeners;

import com.badlogic.gdx.utils.Array;

import io.anuke.koru.entities.KoruEntity;

public class CollisionHandler{
	private Array<CollisionListener> listeners = new Array<CollisionListener>();

	public CollisionHandler() {
		addListener(new PhysicsListener());
		addListener(new DamageListener());
		addListener(new ProjectileListener());
	}

	public void dispatchEvent(KoruEntity entity, KoruEntity other){

		for(CollisionListener listener : listeners){
			if(listener.accept(entity, other)){
				
				listener.contact(entity, other);
				
				listener.collision(entity, other);
			}

			if(listener.accept(other, entity)){
				listener.contact(other, entity);
			}
		}
	}

	public void addListener(CollisionListener listener){
		listeners.add(listener);
	}
}
