package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

@Synced
public class VelocityComponent implements Component{
	public Vector2 velocity = new Vector2();
	public float drag = 1f;
	
	public VelocityComponent setDrag(float drag){
		this.drag = drag;
		return this;
	}
}
