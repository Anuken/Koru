package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.renderers.EntityRenderer;
import io.anuke.layer3d.LayerList;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public LayerList list = new LayerList();

	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
}
