package io.anuke.koru;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pools;

import io.anuke.koru.generation.MaterialManager;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.Generator;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.World;
import io.anuke.ucore.Noise;

public class DefaultGenerator implements Generator{
	World world;
	KoruServer server;

	public DefaultGenerator(World world){
		this.world = world;
		server = KoruServer.instance();
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
		//if(Math.random() < 0.05) tile.setBlockMaterial(Materials.next(Materials.pinetree1, 4));
		if(Math.random() < 0.04) tile.setBlockMaterial(MaterialManager.instance().getGeneratedMaterial(MathUtils.random(0, 4)));
		if(Math.random() < 0.1) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
		if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.fern1, 3));
		if(Math.random() < 0.01) tile.setBlockMaterial(Materials.next(Materials.koru1, 3));
		if(Math.random() < 0.002) tile.setBlockMaterial(Materials.pinesapling);
		if(Math.random() < 0.001) tile.setMaterial(Materials.pinecones);

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
}
