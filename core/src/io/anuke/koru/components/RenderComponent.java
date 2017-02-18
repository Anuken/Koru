package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.renderers.EntityRenderer;
import io.anuke.ucore.spritesystem.RenderableList;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public RenderableList list = new RenderableList();
	public int direction;
	public float walkframe = 0;
	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
}
