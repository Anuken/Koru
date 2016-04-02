package net.pixelstatic.koru.sprites;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class LayerGroup{
	public ObjectMap<String, Layer> layers = new ObjectMap<String, Layer>();
	//public ObjectMap<String, Boolean> update = new ObjectMap<String, Boolean>();
	
	public Layer layer(String region){
		return layer(region, region);
	}
	
	//name is layer alias, region is the region
	public Layer layer(String name, String region){
		if(layers.containsKey(name)){
			return layers.get(name);
		}else{
			Layer layer = Layer.obtainLayer();
			layers.put(name, layer);
			layer.region = region;
			return layer;
		}
	}
	
	public Values<Layer> values(){
		return layers.values();
	}
	
	//public boolean isUpdated(String region){
	//	
	//}
	
	public void update(float x, float y){
		for(Layer layer : layers.values()){
			layer.update(x, y);
		}
	}
}
