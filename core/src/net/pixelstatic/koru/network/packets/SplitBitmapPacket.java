package net.pixelstatic.koru.network.packets;

import com.badlogic.gdx.utils.ObjectMap;

public class SplitBitmapPacket{
	public byte[] data;
	public int id;
	
	public static class Header{
		public ObjectMap<Byte, Integer> colors;
		public int width, height, id;
	}
	
}
