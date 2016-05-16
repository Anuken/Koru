package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.SyncBuffer.Synced;
import net.pixelstatic.koru.world.World;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

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
