package net.pixelstatic.koru.sprites;

import java.util.Arrays;
import java.util.Comparator;


public class PooledLayerList{
	static final int size = 6000;
	protected LayerPool pool = new LayerPool();
	public Layer[] layers;
	public int count;
	

	public PooledLayerList(){
		layers = new Layer[size];
		Layer.list = this;
	}
	
	public void add(Layer layer){
		layers[count] = layer;
		count++;
		if(count == layers.length) throw new ArrayIndexOutOfBoundsException("Critical error: Layer index out of bounds!");
	}
	
	public void sort(){
		Arrays.sort(layers, new Comparator<Layer>(){
			@Override
			public int compare(Layer o1, Layer o2){
				if(o1 == null && o2 == null){
					return 0;
				}
				if(o1 == null){
					return 1;
				}
				if(o2 == null){
					return -1;
				}
				return o1.compareTo(o2);
			}
		});
	}

	
	public synchronized Layer getLayer(){
		Layer layer = pool.obtain();
		return layer;
	}
	
	public void clear(){
		for(int i = 0;i < size;i ++){
			if(layers[i] != null && layers[i].temp) layers[i].free();
			layers[i] = null;
		}
		count = 0;
	}

}
