package io.anuke.koru.components;

import io.anuke.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

@Synced
public class ParticleComponent implements Component{
	public int colorstart, colorend;
	public String name = "spark";
	public float gravity = 1f, velocity = 1f;

	public ParticleComponent set(String name, Color start, Color end){
		this.name = name;
		colorstart = Color.rgba8888(start);
		colorend = Color.rgba8888(end);
		return this;
	}

	public ParticleComponent set(String name, Color color){
		return set(name, color, color);
	}
	
	public ParticleComponent setSpeed(float gravity, float velocity){
		this.gravity = gravity;
		this.velocity = velocity;
		return this;
	}

	public Color getStartColor(){
		return new Color(colorstart);
	}

	public Color getEndColor(){
		return new Color(colorend);
	}
}
