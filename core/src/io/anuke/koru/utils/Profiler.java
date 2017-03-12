package io.anuke.koru.utils;

import com.badlogic.gdx.Gdx;

public class Profiler{
	public static long renderTime = 0;
	public static long totalTime = 0;
	public static long engineTime = 0;
	public static long moduleTime = 0;
	public static long worldTime = 0;
	public static long uiTime = 0;
	public static long networkTime = 0;
	
	public static boolean update(){
		return Gdx.graphics.getFrameId()%300==0;
	}
}
