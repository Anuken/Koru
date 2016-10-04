package io.anuke.koru.network;

import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.IntSet.IntSetIterator;

public class BitmapData{
	final public ConcurrentHashMap<Byte, Integer> colors;
	final public byte[] data;
	final public int width, height;
	transient int byteIndex = 0;
	
	public BitmapData(Pixmap pixmap){
		data = new  byte[pixmap.getWidth()*pixmap.getHeight()];
		IntSet colorset = new IntSet();
		colors = new ConcurrentHashMap<Byte, Integer>();
		this.width = pixmap.getWidth();
		this.height = pixmap.getHeight();
		
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getHeight(); y ++){
				int color = pixmap.getPixel(x, y);
				colorset.add(color);
			}
		}
		
		byte index = 0;
		IntSetIterator it = colorset.iterator();
		IntArray array = it.toArray();
		for(int i : array.items){
			colors.put(index ++, i);
		}
		
		ConcurrentHashMap<Integer, Byte> rmap = new ConcurrentHashMap<Integer, Byte>();
		for(Byte b : colors.keySet()){
			rmap.put(colors.get(b), b);
		}
		
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getHeight(); y ++){
				Byte b = rmap.get(pixmap.getPixel(x, y));
				data[x + y*pixmap.getWidth()] = (b == null ? 5 : b);
			}
		}
	}
	
	public BitmapData(int width, int height, ConcurrentHashMap<Byte, Integer> colors){
		this.width = width;
		this.height = height;
		this.colors = colors;
		this.data = new byte[width*height];
	}
	
	public void pushBytes(byte[] bytes){
		System.arraycopy(bytes, 0, data, byteIndex, bytes.length);
		byteIndex += bytes.length;
	}
	
	public boolean isDone(){
		return byteIndex >= data.length;
	}
	
	public Pixmap toPixmap(){
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getHeight(); y ++){
				Integer i = colors.get(data[x + y*width]);
				pixmap.drawPixel(x, y, i);
			}
		}
		return pixmap;
	}
}
