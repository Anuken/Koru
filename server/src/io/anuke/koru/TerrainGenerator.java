package io.anuke.koru;

import io.anuke.koru.world.Generator;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.Noise;

public class TerrainGenerator implements Generator{
	@Override
	public Tile generate(int x, int y){
		x += 99999;
		y += 99999;

		Tile tile = new Tile();
		float e = getElevation(x, y);
		float t = getTemperature(x, y);

		if(t < 0.7 && t > 0.4 && e < 0.15){
			tile.setMaterial(Materials.burntgrass);
			if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.drybush1, 3));
			
			if(Math.random() < 0.01) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
			if(Math.random() < 0.1) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
			
		}else if(t > 0.7){
			if(e > 0.2f){
				tile.setMaterial(Materials.blackrock);
				if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.blackrock1, 4));
			}else{
				if(t > 0.74){
					tile.setMaterial(Materials.sand);
				}else{
					tile.setMaterial(Materials.burntgrass2);
					if(Math.random() < 0.04) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
				}
				if(Math.random() < 0.02) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
			}
		}else{

			if(e < 0.25f){
				tile.setMaterial(Materials.grass);
			}else if(e < 0.4f){
				tile.setMaterial(Materials.grass);
			}else if(e < 0.5f){
				tile.setMaterial(Materials.stone);
			}else if(e < 1f){
				tile.setMaterial(Materials.ice);
			}

			if(e < 0.35f || (e < 0.4f && Math.random() < (0.45f - e) * 200)){
				if(Math.random() < 0.1 && e > 0.03f)
					tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
				if(Math.random() < 0.03)
					tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));

				if(Math.random() < 0.2 && e < 0.1f)
					tile.setBlockMaterial(Materials.next(Materials.wheatgrass1, 3));
				if(Math.random() < 0.04 && e < 0.15f)
					tile.setBlockMaterial(Materials.next(Materials.wheatgrass1, 3));

				if(Math.random() < 0.02 && e < 0.12f)
					tile.setBlockMaterial(Materials.next(Materials.bush1, 3));

				if(Math.random() < 0.03 * e * 4)
					tile.setBlockMaterial(Materials.next(Materials.fern1, 3));
				if(Math.random() < 0.01 * e * 4)
					tile.setBlockMaterial(Materials.next(Materials.koru1, 3));
			}

			if(e > 0.12f && e < 0.18f && Noise.normalNoise(x, y, 500, 30) + Noise.normalNoise(x, y, 9, 4) > 3){
				if(rand() < 0.026)
					tile.setMaterial(Materials.next(Materials.oaktree1, 6));
				if(rand() < 0.02)
					tile.setMaterial(Materials.next(Materials.mushy1, 8));
				if(Math.random() < 0.05)
					tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));

			}

			if(e > 0.17f && e < 0.4f && Math.random() < e / 10){
				tile.setMaterial(Materials.next(Materials.pinetree1, 4));
				if(Math.random() < 0.002)
					tile.setBlockMaterial(Materials.pinesapling);
			}

			if(e < 0.1f && Noise.normalNoise(x, y, 120, 13) + Noise.normalNoise(x, y, 5, 4) > 4 && t < 0.3)
				tile.setMaterial(Materials.shortgrassblock);

			if(e < 0.1f && Noise.normalNoise(x, y, 120, 13) + Noise.normalNoise(x, y, 5, 4) > 4.5 && t < 0.3)
				tile.setMaterial(Materials.grassblock);
		}

		return tile;
	}

	public float getElevation(int x, int y){
		x += 999999;
		y += 999999;

		double elevation = 1f;

		elevation += (Noise.normalNoise(x, y, 4f, 0.5f));
		elevation += (Noise.normalNoise(x, y, 10f, 1f));
		elevation += (Noise.normalNoise(x, y, 60f, 1f));
		elevation += (Noise.normalNoise(x, y, 500f, 3f));
		elevation += (Noise.normalNoise(x, y, 4000f, 6f));
		elevation += (Noise.normalNoise(x, y, 2000f, 8f));
		elevation += (Noise.normalNoise(x + 9999, y + 9999, 1000f, 4f));
		elevation /= 17.0;

		return (float) elevation;

	}

	public float getTemperature(int x, int y){
		x += 99999 * 2;
		y += 99999 * 2;

		double temp = 0f;

		temp += (Noise.normalNoise(x, y, 4f, 0.1f));
		temp += (Noise.normalNoise(x, y, 10f, 0.17f));
		temp += (Noise.normalNoise(x, y, 600f, 2f));
		temp += (Noise.normalNoise(x, y, 3000f, 4f));
		temp += (Noise.normalNoise(x, y, 5000f, 5f));
		temp += (Noise.normalNoise(x, y, 2000f, 3f));
		temp += (Noise.normalNoise(x, y, 1000f, 3f));

		temp /= 6.0;

		return (float) temp;
	}

	double rand(){
		return Math.random();
	}
}
