package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

@Synced
public class ParticleComponent implements Component{
	public int color;
	public String name = "spark";
	
	public void set(String name, Color color){
		this.name = name;
		this.color = Color.rgba8888(color);
	}
	
	public Color getColor(){
		return new Color(color);
	}
}
