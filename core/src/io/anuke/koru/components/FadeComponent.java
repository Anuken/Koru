package io.anuke.koru.components;

import io.anuke.koru.network.SyncData.Synced;

@Synced
public class FadeComponent implements KoruComponent{
	public float lifetime, life;
	public boolean render;
	
	public FadeComponent(float lifetime){
		this.lifetime = lifetime;
	}
	
	public float scaled(){
		return 1f-life/lifetime;
	}
	
	public FadeComponent enableRender(){
		render = true;
		return this;
	}
	
	public FadeComponent(){}
}
