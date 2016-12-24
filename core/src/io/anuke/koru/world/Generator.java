package io.anuke.koru.world;

import io.anuke.koru.modules.World;

public interface Generator{
	
	public default void generateChunk(Chunk chunk){
		for(int x = 0; x < World.chunksize; x ++){
			for(int y = 0; y < World.chunksize; y ++){
				chunk.tiles[x][y] = generate(chunk.worldX() + x,chunk.worldY() + y);
			}
		}
	}
	
	public Tile generate(int x, int y);
}
