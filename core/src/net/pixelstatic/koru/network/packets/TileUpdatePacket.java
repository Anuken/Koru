package net.pixelstatic.koru.network.packets;

import net.pixelstatic.koru.world.Tile;

public class TileUpdatePacket{
	public int x,y;
	public Tile tile;
	
	public TileUpdatePacket(int x, int y, Tile tile){
		this.tile = tile;
		this.x = x;
		this.y = y;
	}
	
	public TileUpdatePacket(){
		
	}
}
