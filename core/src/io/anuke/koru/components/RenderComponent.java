package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.renderers.EntityRenderer;
import io.anuke.ucore.spritesort.DrawPointerList;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public DrawPointerList list = new DrawPointerList();
	public int direction;
	public float walkframe = 0;
	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
}
