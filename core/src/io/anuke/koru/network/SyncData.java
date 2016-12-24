package io.anuke.koru.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class SyncData{
	public long id;
	
	public SyncData(long id){
		this.id = id;
	}
	
	public SyncData(){};
	
	public static class PositionSyncData extends SyncData{
		public float x,y;
		
		public PositionSyncData(long id, float x, float y){
			super(id);
			this.x = x;
			this.y = y;
		}
		
		public PositionSyncData(){}
	}
	
	public static class PlayerSyncData extends SyncData{
		public float x,y, mouse;
		public int direction;
		
		public PlayerSyncData(long id, float x, float y, float mouse, int o){
			super(id);
			this.x = x;
			this.y = y;
			this.mouse = mouse;
			this.direction = o;
		}
		
		public PlayerSyncData(){}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Synced{}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Transient{}
}
