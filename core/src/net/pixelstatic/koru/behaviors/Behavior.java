package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.components.BehaviorComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.systems.KoruEngine;

public abstract class Behavior{
	protected KoruEntity entity;
	protected KoruEngine engine;
	
	public final void internalUpdate(KoruEntity entity,  KoruEngine engine){
		this.entity = entity;
		this.engine = engine;
		this.update();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Behavior> T getBehavior(Class<T> c, String name){
		return (T)entity.mapComponent(BehaviorComponent.class).behaviors.get(name);
	}
	
	public <T extends Behavior> T getBehavior(Class<T> c){
		return c.cast(getBehavior(c, c.getSimpleName()));
	}
	
	abstract protected void update();
}
