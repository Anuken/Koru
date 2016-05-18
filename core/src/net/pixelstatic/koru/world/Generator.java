package net.pixelstatic.koru.world;

import net.pixelstatic.utils.Noise;

import com.badlogic.gdx.utils.Pools;

public class Generator{
	World world;

	public Generator(World world){
		this.world = world;
	}
	
	public void generateSpawn(){
		if(world.totalChunks() != 0) return;
		world.generateChunk(0, 0);
		world.generateChunk(-1, 0);
		world.generateChunk(0, -1);
		world.generateChunk(-1, -1);
	}

	private Tile generate(int x, int y){
		Tile tile = Pools.obtain(Tile.class);
		int seed = 0;

		if(Noise.normalNoise(x + seed, y + seed, 20f, 7f) > 3){
			tile.setMaterial(Material.water);
			return tile;
		}else if(Noise.normalNoise(x + seed, y + seed, 20f, 7f) > 2.7f){
			tile.setMaterial(Material.riveredge);
			return tile;
		}

		if(Noise.normalNoise(x + 100 + seed, y + 100 + seed, 20f, 6f) > 2f){
			tile.setMaterial(Material.stone);
			return tile;
		}

		tile.setTileMaterial(Material.grass);
		if(Math.random() < 0.05) tile.setBlockMaterial(Material.next(Material.pinetree1, 4));
		if(Math.random() < 0.1) tile.setBlockMaterial(Material.next(Material.tallgrass1, 3));
		if(Math.random() < 0.03) tile.setBlockMaterial(Material.next(Material.fern1, 3));
		if(Math.random() < 0.01) tile.setBlockMaterial(Material.next(Material.koru1, 3));
		if(Math.random() < 0.002) tile.setBlockMaterial(Material.pinesapling);
		if(Math.random() < 0.001) tile.setMaterial(Material.pinecones);
		//	if(Math.random() < 0.01)
		//		tile.setMaterial(Material.box);

		//if(Math.random() < 0.1)
		//	world.tiles[x][y].setTileMaterial(Material.stone);

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
						world.tiles[x][y].setMaterial(Material.water);
						continue;
					}else if(Noise.normalNoise(x + seed, y + seed, 20f, 7f) > 2.7f){
						world.tiles[x][y].setMaterial(Material.riveredge);
						continue;
					}
					
					if(Noise.normalNoise(x + 100+seed, y + 100+seed, 20f, 6f) > 2f){
						world.tiles[x][y].setMaterial(Material.stone);
						continue;
					}

					world.tiles[x][y].setTileMaterial(Material.grass);
					if(Math.random() < 0.05) world.tiles[x][y].setBlockMaterial(Material.next(Material.pinetree1, 4));
					if(Math.random() < 0.1) world.tiles[x][y].setBlockMaterial(Material.next(Material.tallgrass1, 3));
					if(Math.random() < 0.03) world.tiles[x][y].setBlockMaterial(Material.next(Material.fern1, 3));
					if(Math.random() < 0.01) world.tiles[x][y].setBlockMaterial(Material.next(Material.koru1, 3));
					if(Math.random() < 0.002) world.tiles[x][y].setBlockMaterial(Material.pinesapling);
					if(Math.random() < 0.001) world.tiles[x][y].setMaterial(Material.pinecones);
					//	if(Math.random() < 0.01)
					//		world.tiles[x][y].setMaterial(Material.box);

					//if(Math.random() < 0.1)
					//	world.tiles[x][y].setTileMaterial(Material.stone);

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
