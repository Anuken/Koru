package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.modules.World;
import io.anuke.koru.network.SyncBuffer.Synced;

@Synced
public class PositionComponent implements Component{
	public float x,y;
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void set(PositionComponent position){
		set(position.x, position.y);
	}
	
	public void add(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public float sqdist(float x, float y){
		return Math.max(Math.abs(this.x - x), Math.abs(this.y - y));
	}
	
	public float dist(PositionComponent other){
		return Vector2.dst(x, y, other.x, other.y);
	}
	
	public int blockX(){
		return World.tile(x);
	}
	
	public int blockY(){
		return World.tile(y);
	}
	
	public String toString(){
		return x + ", " + y;
	}
}
