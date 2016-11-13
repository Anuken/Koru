package io.anuke.koru.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class SyncBuffer{
	public long id;
	
	public SyncBuffer(long id){
		this.id = id;
	}
	
	public SyncBuffer(){};
	
	public static class PositionSyncBuffer extends SyncBuffer{
		public float x,y;
		
		public PositionSyncBuffer(long id, float x, float y){
			super(id);
			this.x = x;
			this.y = y;
		}
		
		public PositionSyncBuffer(){}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Synced{}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Transient{}
}
