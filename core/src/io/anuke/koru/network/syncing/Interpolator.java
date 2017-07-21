package io.anuke.koru.network.syncing;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.Koru;
import io.anuke.ucore.ecs.Spark;

public class Interpolator{
	static final float correctrange = 20f;
	static Vector2 temp1 = new Vector2();
	static Vector2 temp2 = new Vector2();
	static float alpha = 0.23f;
	long lastupdate = -1;
	float updateframes = 1f;
	float lastx, lasty;

	public void push(Spark e, float x, float y){
		if(lastupdate != -1) updateframes = ((System.currentTimeMillis() - lastupdate) / 1000f) * 60f;
		
		lastupdate = System.currentTimeMillis();
		lastx = x - e.pos().x;
		lasty = y - e.pos().y;
		
		if(Math.abs(e.pos().x - x) > correctrange || Math.abs(e.pos().y - y) > correctrange){
			e.pos().set(x, y);
			lastx = 0;
			lasty = 0;
		}else if(Vector2.dst(e.pos().x, e.pos().y, x, y) < 0.15f){
			e.pos().set(x, y);
			lastx = 0;
			lasty = 0;
		}
	}
	
	public void update(Spark entity){
		temp1.set(entity.pos().x, entity.pos().y);
		temp2.set(lastx + entity.pos().x,lasty + entity.pos().y);
		temp1.interpolate(temp2, alpha*Koru.delta(), Interpolation.linear);
		entity.pos().set(temp1.x, temp1.y);
	}
	
	public float elapsed(){
		return Math.min((((System.currentTimeMillis() - lastupdate) / 1000f) * 60f) / updateframes, 1.0f);
	}
}
