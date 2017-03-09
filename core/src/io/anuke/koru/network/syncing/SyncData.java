package io.anuke.koru.network.syncing;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.anuke.koru.entities.KoruEntity;

public class SyncData{
	public long id;
	public Object[] objects;
	
	public SyncData(KoruEntity entity, Object... objects){
		this.id = entity.getID();
		this.objects = objects;
	}
	
	private SyncData(){}
	
	public <T> T get(int index){
		return (T)objects[index];
	}
	
	public float x(){
		return get(0);
	}
	
	public float y(){
		return get(1);
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Synced{}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Transient{}
}
