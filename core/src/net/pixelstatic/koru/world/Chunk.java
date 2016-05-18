package net.pixelstatic.koru.world;

import com.badlogic.gdx.utils.Pool.Poolable;
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
		//Koru.log("--start--");
		//Koru.log("in: " + worldx);
	//	if(worldx < 0) worldx = World.chunksize - Math.abs(worldx % World.chunksize);
		//if(worldy < 0) worldy = World.chunksize - Math.abs(worldy % World.chunksize);
		//Koru.log("out: "+worldx + " coords are " + x + ", " + y);
		int tx = worldx - worldX(), ty = worldy -worldY();
		return tiles[tx][ty];
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
