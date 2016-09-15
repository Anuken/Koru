package io.anuke.koru.network;

import io.anuke.koru.entities.KoruEntity;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class Interpolator{
	static final float correctrange = 100f;
	static Vector2 temp1 = new Vector2();
	static Vector2 temp2 = new Vector2();
	long lastupdate = -1;
	float updateframes = 1f;
	float lastx, lasty;

	public void push(KoruEntity e, float x, float y){
		if(lastupdate != -1) updateframes = ((System.currentTimeMillis() - lastupdate) / 1000f) * 60f;
		lastupdate = System.currentTimeMillis();
		lastx = x - e.getX();
		lasty = y - e.getY();
		
		if(Math.abs(e.getX() - x) > correctrange || Math.abs(e.getY() - y) > correctrange){
			e.position().set(x, y);
			lastx = 0;
			lasty = 0;
		}
	}
	
	public void update(KoruEntity entity){
		temp1.set(entity.getX(), entity.getY());
		temp2.set(lastx + entity.getX(),lasty + entity.getY());
		temp1.interpolate(temp2, 0.15f, Interpolation.linear);
		entity.position().set(temp1.x, temp1.y);
	}
	
	public float elapsed(){
		return Math.min((((System.currentTimeMillis() - lastupdate) / 1000f) * 60f) / updateframes, 1.0f);
	}
}
