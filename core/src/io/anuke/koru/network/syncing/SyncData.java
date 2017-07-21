package io.anuke.koru.network.syncing;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.anuke.ucore.ecs.Spark;

public class SyncData{
	public long id;
	public Object[] objects;
	
	public SyncData(Spark spark, Object... objects){
		this.id = spark.getID();
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
	
	/**Components marked with this annotation are serialized.*/
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Synced{}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Transient{}
}
