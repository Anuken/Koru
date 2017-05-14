package io.anuke.koru.components;

import com.badlogic.gdx.math.Rectangle;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.systems.CollisionSystem;

@Synced
public class ColliderComponent implements KoruComponent{
	private transient Rectangle bounds = new Rectangle();
	/**If trigger is true, it doesn't collider with terrain*/
	public transient boolean trigger = false;
	public transient float terrainScl = 0.7f; //how much smaller the hitbox becomes when colliding with terrain
	public transient boolean grounded = true;
	
	public transient float width;
	public transient float height;
	
	
	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
	}
	
	public void move(KoruEntity entity, float dx, float dy){
		entity.position().add(dx, 0);
		Koru.getEngine().getSystem(CollisionSystem.class).correctPosition(entity, this);
		entity.position().add(0, dy);
		Koru.getEngine().getSystem(CollisionSystem.class).correctPosition(entity, this);
	}
	
	public Rectangle getBounds(KoruEntity entity){
		bounds.setSize(width, height);
		if(grounded){
			bounds.setCenter(entity.getX(), entity.getY()+height/2);
		}else{
			bounds.setCenter(entity.getX(), entity.getY());
		}
		return bounds;
	}
	
	public Rectangle getTerrainBounds(KoruEntity entity){
		bounds.setSize(width, height*terrainScl);
		if(grounded){
			bounds.setCenter(entity.getX(), entity.getY()+height/2);
		}else{
			bounds.setCenter(entity.getX(), entity.getY());
		}
		return bounds;
	}
}
