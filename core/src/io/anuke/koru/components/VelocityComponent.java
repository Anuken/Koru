package io.anuke.koru.components;

import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.network.SyncData.Synced;

@Synced
public class VelocityComponent implements KoruComponent{
	public Vector2 velocity = new Vector2();
	public float drag = 0f;
	public float limit = 2f;
	
	public VelocityComponent set(float drag, float limit){
		this.drag = drag;
		this.limit = limit;
		return this;
	}
	
	public VelocityComponent setDrag(float drag){
		this.drag = drag;
		return this;
	}
}
