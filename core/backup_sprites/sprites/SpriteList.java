package net.pixelstatic.koru.sprites;

import java.util.Arrays;
import java.util.Comparator;

public class SpriteList{
	static final int size = 6000;
	public int count;
	public SpriteLayer[] layers;
	
	public SpriteList(){
		layers = new SpriteLayer[size];
	}
	
	public void add(SpriteLayer layer){
		layers[count] = layer;
		count++;
		if(count == layers.length) throw new ArrayIndexOutOfBoundsException("Critical error: Layer index out of bounds!");
	}
	
	public void sort(){
		Arrays.sort(layers, new Comparator<SpriteLayer>(){
			@Override
			public int compare(SpriteLayer o1, SpriteLayer o2){
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
	
	public void clear(){
		for(int i = 0;i < size;i ++){
			layers[i] = null;
		}
		count = 0;
	}
}
