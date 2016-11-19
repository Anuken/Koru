package io.anuke.koru;

import com.badlogic.gdx.utils.Pools;

import io.anuke.koru.world.Generator;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.noise.Noise;

public class SimpleGenerator implements Generator{

	public Tile generate(int x, int y){
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
		
		if(Math.random() < 0.1) tile.setBlockMaterial(Materials.grassblock);
		if(Math.random() < 0.1) tile.setBlockMaterial(Materials.shortgrassblock);
		
		if(Math.random() < 0.1) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
		if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.fern1, 3));
		if(Math.random() < 0.01) tile.setBlockMaterial(Materials.next(Materials.koru1, 3));
		if(Math.random() < 0.002) tile.setBlockMaterial(Materials.pinesapling);
		if(Math.random() < 0.001) tile.setMaterial(Materials.pinecones);

		tile.changeEvent();
		return tile;
	}
}
