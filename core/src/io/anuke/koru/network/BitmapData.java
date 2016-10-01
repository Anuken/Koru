package io.anuke.koru.network;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class BitmapData{
	final public ObjectMap<Byte, Integer> colors;
	final public byte[] data;
	final public int width, height;
	
	public BitmapData(Pixmap pixmap){
		data = new  byte[pixmap.getWidth()*pixmap.getHeight()];
		ObjectSet<Integer> colorset = new ObjectSet<Integer>();
		colors = new ObjectMap<Byte, Integer>();
		this.width = pixmap.getWidth();
		this.height = pixmap.getHeight();
		
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getWidth(); y ++){
				int color = pixmap.getPixel(x, y);
				colorset.add(color);
			}
		}
		
		byte index = 0;
		for(Integer i : colorset){
			colors.put(index ++, i);
		}
		
		ObjectMap<Integer, Byte> rmap = new ObjectMap<Integer, Byte>();
		for(Byte b : colors.keys()){
			rmap.put(colors.get(b), b);
		}
		
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getWidth(); y ++){
				data[x + y*pixmap.getHeight()] = rmap.get(pixmap.getPixel(x, y));
			}
		}
	}
	
	public Pixmap toPixmap(){
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		for(int x = 0; x < pixmap.getWidth(); x ++){
			for(int y = 0; y < pixmap.getWidth(); y ++){
				pixmap.drawPixel(x, y, colors.get(data[x + y*height]));
			}
		}
		return pixmap;
	}
}
