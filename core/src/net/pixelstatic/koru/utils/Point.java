package net.pixelstatic.koru.utils;

import com.badlogic.gdx.math.Vector2;

public class Point{
	public int x,y;
	
	public Point(){
		
	}
	
	public Point(int x, int y){
		set(x,y);
	}
	
	/**
	 * Utility distance method.
	 * @return distance from x, y to this point's x, y.
	 */
	public float dist(int x, int y){
		return Vector2.dst(x, y, this.x, this.y);
	}
	
	public boolean equals(int x, int y){
		return this.x == x && this.y == y;
	}
	
	public void set(int x, int y){
		this.x =x;
		this.y =y;
	}
	
	public String toString(){
		return "[" + x + ", " + y + "]";
	}
}
