package io.anuke.koru.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.graphics.PixmapUtils;

public class Cursors{
	private static ObjectMap<String, Cursor> cursors = new ObjectMap<>();
	private static String cursor;
	
	public static void setCursor(String name){
		cursor = name;
	}
	
	private static Cursor loadCursor(String name){
		Cursor cursor = cursors.get(name);
		
		if(cursor == null){
			Texture texture = new Texture("cursors/"+name+".png");
			texture.getTextureData().prepare();
			
			Pixmap pixmap = texture.getTextureData().consumePixmap();
			Pixmap out = PixmapUtils.outline(pixmap, new Color(0,0,0,0.1f));
			Pixmap out2 = PixmapUtils.scale(out, 4);
			
			out.dispose();
			pixmap.dispose();
			
			cursor = Gdx.graphics.newCursor(out2, out2.getWidth()/2, out2.getHeight()/2);
			cursors.put(name, cursor);
		}
		
		return cursor;
	}
	
	public static void updateCursor(){
		if(cursor != null){
			Gdx.graphics.setCursor(loadCursor(cursor));
		}
	}
}
