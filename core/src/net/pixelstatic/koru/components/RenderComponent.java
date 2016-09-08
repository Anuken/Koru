package net.pixelstatic.koru.components;

import net.pixelstatic.gdxutils.spritesystem.RenderableGroup;
import net.pixelstatic.koru.renderers.EntityRenderer;

import com.badlogic.ashley.core.Component;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public RenderableGroup group = new RenderableGroup();

	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
}
