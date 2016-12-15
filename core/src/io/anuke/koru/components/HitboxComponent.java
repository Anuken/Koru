package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.koru.utils.Hitbox;

public class HitboxComponent implements Component{
	public final static float sleepduration = 80f;
	public float height; //coords of top of entity
	public Hitbox entityhitbox = new Hitbox();
	public Hitbox terrainhitbox = new Hitbox();
	public boolean collideterrain = false;
	public boolean sleeping;
	public float sleeptime = sleepduration;
	
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
