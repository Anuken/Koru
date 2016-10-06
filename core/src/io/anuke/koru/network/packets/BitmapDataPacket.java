package io.anuke.koru.network.packets;

public class BitmapDataPacket{
	public byte[] data;
	public int id;
	
	public static class Header{
		public int[] colors;
		public int width, height, id;
	}
}
