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
	@Override
	public <T extends Component> T getComponent (Class<T> componentClass) {
		return get(componentClass);
	}
	
	@Override
	public Component remove (Class<? extends Component> componentClass) {
		throw new IllegalArgumentException("Removing components is not supported.");
	}
	
	/**Equivalent to getComponent.*/
	public <T>T get(Class<T> c){
		return (T)(Mappers.get(c, this));
	}
	
	public <T> boolean hasComponent(Class<T> c){
		return (Mappers.has(c, this));
	}
	
	public KoruEntity(Class<? extends EntityType> type){
		this(type, true);
	}

	public static KoruEntity loadedEntity(Class<? extends EntityType> type, long id){
		KoruEntity entity = new KoruEntity(type, false);
		entity.id = id;
		return entity;
	}

	private KoruEntity(Class<? extends EntityType> type, boolean initialize){
		id = nextID ++;
		this.type = type;
		Array<KoruComponent> components = getType().components().list;
		
		for(Component component : components)
			this.add(component);
		
		for(KoruComponent component : components)
			component.onAdd(this);
		
		if(initialize)
		EntityTypes.get(type).init(this);
	}
	
	private KoruEntity(){}
	
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
		KoruEngine.instance().addEntity(this);
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
		KoruEngine.instance().removeEntity(this);
	}
	
	/**Only used for resetting the player's ID on reconnect*/
	public void resetID(long id){
		this.id = id;
		nextID ++;
	}

	boolean server(){
		return IServer.active();
	}

	public String toString(){
		return this.getType() + " #" + id;
	}
}
