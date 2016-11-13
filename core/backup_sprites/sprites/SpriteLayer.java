package net.pixelstatic.koru.sprites;

import net.pixelstatic.koru.modules.Renderer;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteLayer implements Comparable<SpriteLayer>{
	public static Renderer renderer;
	public Sprite sprite;
	float layer;
	
	public SpriteLayer(String region){
		sprite = new Sprite(renderer.getRegion(region));
	}
	
	public SpriteLayer set(float x, float y){
		sprite.setPosition(x - sprite.getWidth()/2f, y - sprite.getHeight()/2f);
		return this;
	}
	/*
	public SpriteLayer add(){
		renderer.sprites().add(this);
		return this;
	}
*/
	@Override
	public int compareTo(SpriteLayer s){
		if(s.layer == this.layer){
			return 0;
		}else if(s.layer > this.layer){
			return -1;
		}else{
			return 1;
		}
	}
}
