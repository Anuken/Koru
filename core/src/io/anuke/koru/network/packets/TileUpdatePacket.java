package io.anuke.koru.network.packets;

import io.anuke.koru.world.Tile;

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
