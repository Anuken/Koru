package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.network.SyncBuffer.Synced;

@Synced
public class VelocityComponent implements Component{
	public Vector2 velocity = new Vector2();
	public float drag = 1f;
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
