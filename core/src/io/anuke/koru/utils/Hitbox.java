package io.anuke.koru.utils;

import io.anuke.koru.entities.KoruEntity;

import com.badlogic.gdx.math.Rectangle;

public class Hitbox{
	public Rectangle rect = new Rectangle();
	public float centerx, centery;
	

	public boolean collides(Hitbox other){
		return this.rect.overlaps(other.rect);
	}

	public boolean collides(Rectangle rect){
		return this.rect.overlaps(rect);
	}
	
	public void update(KoruEntity entity){
		update(entity,0,0);
	}

	public void update(KoruEntity entity, float nx, float ny){
		rect.setCenter(entity.getX()+nx, entity.getY() + ny + centery);
	}
	
	public void setCenter(float x, float y){
		centerx = x;
		centery = y;
	}
	
	public void alignBottom(){
		centery = rect.height/2;
	}
}
