package net.pixelstatic.koru.ai;

import net.pixelstatic.koru.world.Physics;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;

public class Raycaster implements RaycastCollisionDetector<Vector2>{

	@Override
	public boolean collides(Ray<Vector2> ray){
		return Physics.cast(ray.start.x, ray.start.y, ray.end.x, ray.end.y);
	}

	@Override
	public boolean findCollision(Collision<Vector2> collision, Ray<Vector2> ray){
		Vector2 v = Physics.vectorCast(ray.start.x, ray.start.y, ray.end.x, ray.end.y);
		if(v == null) return false;
		collision.point = v;
		collision.normal = v.nor();
		return true;
	}

}
