package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.renderers.EntityRenderer;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public int direction;
	public float walkframe = 0;
	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
}
