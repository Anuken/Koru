package net.pixelstatic.koru.components;

import net.pixelstatic.koru.renderers.EntityRenderer;
import net.pixelstatic.koru.sprites.Layer;
import net.pixelstatic.koru.sprites.LayerGroup;

import com.badlogic.ashley.core.Component;

public class RenderComponent implements Component{
	public final EntityRenderer renderer;
	public LayerGroup layers = new LayerGroup();
//	public Array<Layer> layers = new Array<Layer>();
	/*
	public Layer sprite(String region){
		Layer layer = Layer.obtainLayer();
		layer.region = region;
		layers.add(layer);
		layer.alignBottom();
		return layer;
	}
	*/
	
	public RenderComponent(EntityRenderer renderer){
		this.renderer = renderer;
	}
	
	public Layer layer(String region){
		return layers.layer(region);
	}
	
	public Layer layer(String name, String region){
		return layers.layer(name, region);
	}
}
