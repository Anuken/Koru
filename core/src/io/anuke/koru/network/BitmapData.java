package io.anuke.koru.network;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectMap;

public class BitmapData{
	final public byte[] data;
	final public int width, height;
	final public int[] colors;
	transient int byteIndex = 0;
	
	public BitmapData(Pixmap pixmap){
		data = new  byte[pixmap.getWidth()*pixmap.getHeight()];
		IntSet colorset = new IntSet();
		this.width = pixmap.getWidth();
		this.height = pixmap.getHeight();
		
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getHeight(); y ++){
				int color = pixmap.getPixel(x, y);
				colorset.add(color);
			}
		}
		
		colors = new int[colorset.size];
		
		byte index = 0;
		for(int i : colorset.iterator().toArray().items){
			colors[index ++] = i;
		}
		
		ObjectMap<Integer, Byte> rmap = new ObjectMap<Integer, Byte>();
		for(byte i = 0; i < colors.length; i ++){
			rmap.put(colors[i], i);
		}
		
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getHeight(); y ++){
				Byte b = rmap.get(pixmap.getPixel(x, y));
				data[x + y*pixmap.getWidth()] = (b == null ? 5 : b);
			}
		}
	}
	
	public BitmapData(int width, int height, int[] colors){
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
				Integer i = colors[data[x + y*width]];
				pixmap.drawPixel(x, y, i);
			}
		}
		return pixmap;
	}
}
