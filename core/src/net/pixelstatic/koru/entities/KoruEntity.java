package net.pixelstatic.koru.entities;

import java.util.HashMap;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.components.GroupComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.server.KoruServer;
import net.pixelstatic.koru.systems.KoruEngine;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

public class KoruEntity extends Entity{
	private static KoruServer server;
	private static KoruEngine engine;
	private EntityType type;
	private static long nextID;
	private long id;

	public static class Mappers{
		public static HashMap<Class<?>, ComponentMapper<?>> map = new HashMap<Class<?>, ComponentMapper<?>>();
		public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);

		public static <T>Component get(Class<? extends Component> c, KoruEntity entity){
			if( !map.containsKey(c)){
				map.put(c, ComponentMapper.getFor(c));
			}
			return c.cast(map.get(c).get(entity));
		}
	}
	
	@SuppressWarnings("unused")
	private KoruEntity(){
	}
	
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
		return this.mapComponent(PositionComponent.class);
	}
	
	public float getX(){
		return this.mapComponent(PositionComponent.class).x;
	}
	
	public float getY(){
		return this.mapComponent(PositionComponent.class).y;
	}
	
	public Group group(){
		return this.mapComponent(GroupComponent.class).group;
	}

	public long getID(){
		return id;
	}

	public <T>T mapComponent(Class<T> c){
		return c.cast(Mappers.get(c.asSubclass(Component.class), this));
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
		server.sendEntity(this);
		return this;
	}
	
	public void removeSelfServer(){
		if(!server()) return;
		server.removeEntity(this);
	}
	
	public void removeSelf(){
		if(!server()){
			engine.removeEntity(this);
		}else{
			engine.removeEntity(this);
		}
	}
	
	//ONLY CALL THIS ON RECONNECT
	public void resetID(long id){
		this.id = id;
		nextID ++;
	}
	
	public static boolean server(){
		return server != null;
	}
	
	public static void setEngine(KoruEngine e){
		engine = e;
	}
	
	public static void setServer(KoruServer e){
		server = e;
	}
	
	public String toString(){
		return "Entity: " + this.type + " #" + id;
	}

}
