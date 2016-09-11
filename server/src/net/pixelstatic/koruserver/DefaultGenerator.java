package net.pixelstatic.koruserver;

import net.pixelstatic.gdxutils.Noise;
import net.pixelstatic.koru.world.*;

import com.badlogic.gdx.utils.Pools;

public class DefaultGenerator implements Generator{
	World world;

	public DefaultGenerator(World world){
		this.world = world;
	}

	private Tile generate(int x, int y){
		Tile tile = Pools.obtain(Tile.class);
		int seed = 0;

		if(Noise.normalNoise(x + seed, y + seed, 20f, 7f) > 3){
			tile.setMaterial(Materials.water);
			return tile;
		}else if(Noise.normalNoise(x + seed, y + seed, 20f, 7f) > 2.7f){
			tile.setMaterial(Materials.riveredge);
			return tile;
		}

		if(Noise.normalNoise(x + 100 + seed, y + 100 + seed, 20f, 6f) > 2f){
			tile.setMaterial(Materials.stone);
			return tile;
		}

		tile.setTileMaterial(Materials.grass);
		if(Math.random() < 0.05) tile.setBlockMaterial(Materials.next(Materials.pinetree1, 4));
		if(Math.random() < 0.1) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
		if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.fern1, 3));
		if(Math.random() < 0.01) tile.setBlockMaterial(Materials.next(Materials.koru1, 3));
		if(Math.random() < 0.002) tile.setBlockMaterial(Materials.pinesapling);
		if(Math.random() < 0.001) tile.setMaterial(Materials.pinecones);
		//	if(Math.random() < 0.01)
		//		tile.setMaterial(Materials.box);

		//if(Math.random() < 0.1)
		//	world.tiles[x][y].setTileMaterial(Materials.stone);

		tile.changeEvent();
		return tile;
	}
	

	public void generateChunk(Chunk chunk){
		for(int x = 0; x < World.chunksize; x ++){
			for(int y = 0; y < World.chunksize; y ++){
				chunk.tiles[x][y] = generate(chunk.worldX() + x,chunk.worldY() + y);
			}
		}
	}
	/*
		public void generate(){
			int seed = 0;
			for(int x = 0;x < World.worldwidth;x ++){
				for(int y = 0;y < World.worldwidth;y ++){
					if(Noise.normalNoise(x + seed, y + seed, 20f, 7f) > 3){
						world.tiles[x][y].setMaterial(Materials.water);
						continue;
					}else if(Noise.normalNoise(x + seed, y + seed, 20f, 7f) > 2.7f){
						world.tiles[x][y].setMaterial(Materials.riveredge);
						continue;
					}
					
					if(Noise.normalNoise(x + 100+seed, y + 100+seed, 20f, 6f) > 2f){
						world.tiles[x][y].setMaterial(Materials.stone);
						continue;
					}

					world.tiles[x][y].setTileMaterial(Materials.grass);
					if(Math.random() < 0.05) world.tiles[x][y].setBlockMaterial(Materials.next(Materials.pinetree1, 4));
					if(Math.random() < 0.1) world.tiles[x][y].setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
					if(Math.random() < 0.03) world.tiles[x][y].setBlockMaterial(Materials.next(Materials.fern1, 3));
					if(Math.random() < 0.01) world.tiles[x][y].setBlockMaterial(Materials.next(Materials.koru1, 3));
					if(Math.random() < 0.002) world.tiles[x][y].setBlockMaterial(Materials.pinesapling);
					if(Math.random() < 0.001) world.tiles[x][y].setMaterial(Materials.pinecones);
					//	if(Math.random() < 0.01)
					//		world.tiles[x][y].setMaterial(Materials.box);

					//if(Math.random() < 0.1)
					//	world.tiles[x][y].setTileMaterial(Materials.stone);

				}
			}
			
			for(int x = 0;x < World.worldwidth;x ++){
				for(int y = 0;y < World.worldwidth;y ++){
					world.tiles[x][y].changeEvent();
				}
			}

			Koru.log("Generated world.");
		}
		*/
}
