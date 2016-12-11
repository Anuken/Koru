package io.anuke.koru.world;

import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.koru.modules.World;

import com.badlogic.gdx.utils.Pools;

public class Chunk implements Poolable{
	public Tile[][] tiles = new Tile[World.chunksize][World.chunksize];
	public int x,y;
	
	public Chunk(){
		
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Tile getWorldTile(int worldx, int worldy){
		int tx = worldx - worldX(), ty = worldy -worldY();
		return tiles[tx][ty];
	}
	
	public void setWorldTile(int worldx, int worldy, Tile tile){
		int tx = worldx - worldX(), ty = worldy -worldY();
		tiles[tx][ty] = tile;
	}
	
	public int worldX(){
		return x * World.chunksize;
	}
	
	public int worldY(){
		return y * World.chunksize;
	}

	@Override
	public void reset(){
		for(int x = 0; x < World.chunksize; x ++){
			for(int y = 0; y < World.chunksize; y ++){
				Pools.free(tiles[x][y]);
				tiles[x][y] = null;
			}
		}
	}
	
	public String toString(){
		return ("[Chunk " + x +", "+ y + "]");
	}
}
