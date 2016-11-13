package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.renderers.EntityRenderer;
import io.anuke.ucore.g3d.ModelGroup;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public ModelGroup models = new ModelGroup();

	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
}
