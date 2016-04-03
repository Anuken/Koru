package net.pixelstatic.koru.utils;

import com.badlogic.gdx.graphics.Color;

public class Colors{
	public static Color color(int r, int g, int b){
		return new Color(r/255f,g/255f,b/255f,1f);
	}
	
	public static Color colora(int r, int g, int b, float brightness){
		return new Color(r/255f+brightness,g/255f+brightness,b/255f+brightness,1f);
	}
}
