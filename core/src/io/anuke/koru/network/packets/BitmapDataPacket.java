package io.anuke.koru.network.packets;

import java.util.concurrent.ConcurrentHashMap;

public class BitmapDataPacket{
	public byte[] data;
	public int id;
	
	public static class Header{
		public ConcurrentHashMap<Byte, Integer> colors;
		public int width, height, id;
	}
}
