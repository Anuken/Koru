package io.anuke.koru.components;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.world.Material;

@Synced
public class ParticleComponent implements KoruComponent{
	public int colorstart, colorend;
	public Material material;
	public String name = "spark";
	public float gravity = 1f, velocity = 1f;

	public ParticleComponent set(String name, Color start, Color end){
		this.name = name;
		colorstart = Color.rgba8888(start);
		colorend = Color.rgba8888(end);
		return this;
	}
	
	public ParticleComponent set(Material material){
		this.material = material;
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
