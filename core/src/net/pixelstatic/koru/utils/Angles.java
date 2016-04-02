package net.pixelstatic.koru.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Angles{
	static Vector2 vector = new Vector2(1,1);
	
	static public float ForwardDistance(float angle1, float angle2){
		if(angle1 > angle2){
			return angle1 - angle2;
		}else{
			return angle2 - angle1;
		}
	}

	static public float BackwardDistance(float angle1, float angle2){
		return 360 - ForwardDistance(angle1, angle2);
	}

	static public float angleDist(float a, float b){
		return Math.min(ForwardDistance(a, b), BackwardDistance(a, b));
	}

	static public float MoveToward(float angle, float to, float turnspeed){
		if(Math.abs(angleDist(angle, to)) < turnspeed){
			return to;
		}
		float speed = turnspeed;

		if(angle > to){
			if(BackwardDistance(angle, to) > ForwardDistance(angle, to)){
				angle -= speed;
			}else{
				angle += speed;
			}
		}else{
			if(BackwardDistance(angle, to) < ForwardDistance(angle, to)){
				angle -= speed;
			}else{
				angle += speed;
			}
		}
		return angle;
	}
	
	static public Vector2 rotate(float x, float y, float angle){
		if(MathUtils.isEqual(angle, 0, 0.001f)) return vector.set(x,y);
		return vector.set(x,y).rotate(angle);
	}
	
	static public Vector2 translation(float angle, float amount){
		if(amount < 0) angle += 180f;
		return vector.setAngle(angle).setLength(amount);
	}

	static public float mouseAngle(OrthographicCamera camera,float cx, float cy){
		float x = 0,y = 0;
		Vector3 vector = camera.project(new Vector3(cx, cy, 0));
		x = vector.x;
		y = vector.y;
		Vector2 v = new Vector2(Gdx.input.getX() - x, Gdx.graphics.getHeight() - Gdx.input.getY() - y);
		return v.angle();
	}
}
