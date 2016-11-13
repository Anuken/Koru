package io.anuke.koru.components;

import io.anuke.koru.utils.Hitbox;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class HitboxComponent implements Component{
	public float height; //coords of top of entity
	public Hitbox entityhitbox = new Hitbox();
	public Hitbox terrainhitbox = new Hitbox();
	public boolean collideterrain = false;
	
	public Rectangle terrainRect(){
		return terrainhitbox.rect;
	}
	
	public Rectangle entityRect(){
		return entityhitbox.rect;
	}
	
	public void alignBottom(){
		terrainhitbox.alignBottom();
		entityhitbox.alignBottom();
	}
}
