package io.anuke.koru.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**why does this even exist?*/
public class Angles{
	static Vector2 vector = new Vector2(1,1);
	
	static public Vector2 rotate(float x, float y, float angle){
		if(MathUtils.isEqual(angle, 0, 0.001f)) return vector.set(x,y);
		return vector.set(x,y).rotate(angle);
	}
	
	static public Vector2 translation(float angle, float amount){
		if(amount < 0) angle += 180f;
		return vector.setAngle(angle).setLength(amount);
	}

	static public float mouseAngle(OrthographicCamera camera,float cx, float cy){
		Vector3 avector = camera.project(new Vector3(cx, cy, 0));
		vector.set(Gdx.input.getX() - avector.x, Gdx.graphics.getHeight() - Gdx.input.getY() - avector.y);
		return vector.angle();
	}
}
