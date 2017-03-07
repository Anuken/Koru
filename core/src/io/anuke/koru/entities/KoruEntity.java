package io.anuke.koru.entities;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.Koru;
import io.anuke.koru.components.*;
import io.anuke.koru.network.IServer;
import io.anuke.koru.systems.KoruEngine;

public class KoruEntity extends Entity{
	private static KoruEngine engine;
	private Class<? extends EntityType> type;
	private static long nextID;
	private long id;
	private transient PositionComponent pos;

	public static class Mappers{
		public static HashMap<Class<?>, ComponentMapper<?>> map = new HashMap<Class<?>, ComponentMapper<?>>();
		public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);

		public static <T> Component get(Class<T> c, KoruEntity entity){
			if( !map.containsKey(c)){
				map.put(c, ComponentMapper.getFor((Class<? extends Component>)c));
			}
			return (Component)(map.get(c).get(entity));
		}
		
		public static <T> boolean has(Class<T> c, KoruEntity entity){
			if( !map.containsKey(c)){
				map.put(c, ComponentMapper.getFor((Class<? extends Component>)c));
			}
			return (map.get(c).has(entity));
		}
	}
	
	/**overridden and now safe to use (?)*/
	public <T extends Component> T getComponent (Class<T> componentClass) {
		return get(componentClass);
	}
	
	/**Equivalent to getComponent.*/
	public <T>T get(Class<T> c){
		return (T)(Mappers.get(c, this));
	}
	
	public <T> boolean hasComponent(Class<T> c){
		return (Mappers.has(c, this));
	}

	private KoruEntity(){}

	public static KoruEntity loadedEntity(Class<? extends EntityType> type, long id){
		KoruEntity entity = new KoruEntity(type);
		entity.id = id;
		return entity;
	}

	public KoruEntity(Class<? extends EntityType> type){
		id = nextID ++;
		this.type = type;
		Array<KoruComponent> components = getType().components().list;
		
		for(Component component : components)
			this.add(component);
		EntityTypes.get(type).init(this);
	}
	
	//Shortcut component getter methods.
	
	public PositionComponent position(){
		if(pos == null) pos = this.get(PositionComponent.class);
		return pos;
	}
	
	public InventoryComponent inventory(){
		return get(InventoryComponent.class);
	}
	
	public ConnectionComponent connection(){
		return get(ConnectionComponent.class);
	}
	
	public RenderComponent renderer(){
		return get(RenderComponent.class);
	}
	
	public ColliderComponent collider(){
		return get(ColliderComponent.class);
	}

	public float getX(){
		return this.get(PositionComponent.class).x;
	}

	public float getY(){
		return this.get(PositionComponent.class).y;
	}

	public long getID(){
		return id;
	}

	public void log(Object object){
		Koru.log("[" + id + "]: " + object);
	}

	public EntityType getType(){
		return EntityTypes.get(type);
	}
	
	public Class<? extends EntityType> getTypeClass(){
		return type;
	}

	public boolean isType(Class<? extends EntityType> type){
		return this.type == type;
	}

	public KoruEntity add(){
		engine.addEntity(this);
		return this;
	}

	public KoruEntity send(){
		IServer.instance().sendEntity(this);
		return this;
	}

	public void removeServer(){
		if( !server()) return;
		IServer.instance().removeEntity(this);
	}

	public void remove(){
		engine.removeEntity(this);
	}
	
	/**Only used for resetting the player's ID on reconnect*/
	public void resetID(long id){
		this.id = id;
		nextID ++;
	}

	boolean server(){
		return IServer.active();
	}

	public static void setEngine(KoruEngine e){
		engine = e;
	}

	public String toString(){
		return this.type + " #" + id;
	}
}
