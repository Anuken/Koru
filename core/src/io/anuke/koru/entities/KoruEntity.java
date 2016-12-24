package io.anuke.koru.entities;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.network.IServer;
import io.anuke.koru.systems.KoruEngine;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public class KoruEntity extends Entity implements QuadTreeObject{
	private static KoruEngine engine;
	private EntityType type;
	private static long nextID;
	private long id;
	private transient PositionComponent pos;

	public static class Mappers{
		public static HashMap<Class<?>, ComponentMapper<?>> map = new HashMap<Class<?>, ComponentMapper<?>>();
		public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);

		@SuppressWarnings("unchecked")
		public static <T> Component get(Class<T> c, KoruEntity entity){
			if( !map.containsKey(c)){
				map.put(c, ComponentMapper.getFor((Class<? extends Component>)c));
			}
			return (Component)(map.get(c).get(entity));
		}
		
		@SuppressWarnings("unchecked")
		public static <T> boolean has(Class<T> c, KoruEntity entity){
			if( !map.containsKey(c)){
				map.put(c, ComponentMapper.getFor((Class<? extends Component>)c));
			}
			return (map.get(c).has(entity));
		}
	}

	private KoruEntity(){}

	public static KoruEntity loadedEntity(EntityType type, long id){
		KoruEntity entity = new KoruEntity(type);
		entity.id = id;
		return entity;
	}

	public KoruEntity(EntityType type){
		id = nextID ++;
		this.type = type;
		Component[] components = type.defaultComponents();
		for(Component component : components)
			this.add(component);
		this.type.init(this);
	}

	public PositionComponent position(){
		if(pos == null) pos = this.mapComponent(PositionComponent.class);
		return pos;
	}
	
	public InventoryComponent inventory(){
		return get(InventoryComponent.class);
	}

	public float getX(){
		return this.mapComponent(PositionComponent.class).x;
	}

	public float getY(){
		return this.mapComponent(PositionComponent.class).y;
	}

	public long getID(){
		return id;
	}

	@SuppressWarnings("unchecked")
	public <T>T mapComponent(Class<T> c){
		return (T)(Mappers.get(c, this));
	}
	
	/**Equivalent to mapComponent.*/
	@SuppressWarnings("unchecked")
	public <T>T get(Class<T> c){
		return (T)(Mappers.get(c, this));
	}
	
	public <T> boolean hasComponent(Class<T> c){
		return (Mappers.has(c, this));
	}

	public void log(Object object){
		Koru.log("[" + id + "]: " + object);
	}

	public EntityType getType(){
		return type;
	}

	public boolean isType(EntityType type){
		return this.type == type;
	}

	public KoruEntity addSelf(){
		engine.addEntity(this);
		return this;
	}

	public KoruEntity sendSelf(){
		IServer.instance().sendEntity(this);
		return this;
	}

	public void removeSelfServer(){
		if( !server()) return;
		IServer.instance().removeEntity(this);
	}

	public void removeSelf(){
		//wat
		if( !server()){
			engine.removeEntity(this);
		}else{
			engine.removeEntity(this);
		}
	}

	//ONLY CALL THIS ON RECONNECT
	//seriously
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

	@Override
	public void getBoundingBox(Rectangle out){
		//not implemented yet
		//should it be?
	}
}
