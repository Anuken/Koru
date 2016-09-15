package io.anuke.koru.components;

import io.anuke.koru.renderers.EntityRenderer;
import net.pixelstatic.gdxutils.spritesystem.RenderableGroup;

import com.badlogic.ashley.core.Component;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public RenderableGroup group = new RenderableGroup();

	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
}
