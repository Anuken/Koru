package io.anuke.koru.components;

import io.anuke.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;

@Synced
public class FadeComponent implements Component{
	public float lifetime, life;
	public boolean render;
	
	public FadeComponent(float lifetime){
		this.lifetime = lifetime;
	}
	
	public FadeComponent enableRender(){
		render = true;
		return this;
	}
	
	public FadeComponent(){}
}
