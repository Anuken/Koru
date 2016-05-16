package net.pixelstatic.koru.world;

public class Chunk{
	public final Tile[][] tiles = new Tile[World.chunksize][World.chunksize];
	public final int x,y;
	
	public Chunk(int x, int y){
		this.x = x;
		this.y = y;
	}
}
