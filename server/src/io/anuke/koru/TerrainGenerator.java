package io.anuke.koru;

import io.anuke.koru.world.Generator;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.Noise;

public class TerrainGenerator implements Generator{
	@Override
	public Tile generate(int x, int y){
		Tile tile = new Tile();
		float e = getElevation(x,y);
		float t = getTemperature(x,y);
		
		if(e < 0.25f){
			tile.setMaterial(Materials.grass);
		}else if(e < 0.4f){
			tile.setMaterial(Materials.grass);
		}else if(e < 0.5f){
			tile.setMaterial(Materials.stone);
		}else if(e < 1f){
			tile.setMaterial(Materials.ice);
		}
		
		if(e < 0.35f || (e < 0.4f && Math.random() < (0.45f-e)*200)){
			if(Math.random() < 0.1) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
			if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.fern1, 3));
			if(Math.random() < 0.01) tile.setBlockMaterial(Materials.next(Materials.koru1, 3));
			if(Math.random() < 0.002) tile.setBlockMaterial(Materials.pinesapling);
		}
		
		if(e < 0.1f && Noise.normalNoise(x, y, 200, 20) + Noise.normalNoise(x, y, 5, 4) > 3)
			tile.setMaterial(Materials.grassblock);
		
		if(e > 0.17f && e < 0.4f && Math.random() < e/10)
			tile.setMaterial(Materials.next(Materials.pinetree1, 4));
		
		return tile;
	}
	
	public float getElevation(int x, int y){
		double elevation = 1f;
		
		elevation += (Noise.normalNoise(x, y, 5f, 1f));
		elevation += (Noise.normalNoise(x, y, 10f, 1f));
		elevation += (Noise.normalNoise(x, y, 60f, 1f));
		elevation += (Noise.normalNoise(x, y, 500f, 3f));
		elevation += (Noise.normalNoise(x, y, 4000f, 6f));
		elevation += (Noise.normalNoise(x, y, 2000f, 8f));
		elevation += (Noise.normalNoise(x+9999, y+9999, 1000f, 4f));
		elevation /= 17.0;
		
		return (float)elevation;
		
	}
	
	public float getTemperature(int x, int y){
		x += 99999;
		y += 99999;
		
		double temp = 0f;
		
		temp += (Noise.normalNoise(x, y, 600f, 2f));
		temp += (Noise.normalNoise(x, y, 3000f, 4f));
		temp += (Noise.normalNoise(x, y, 5000f, 5f));
		temp += (Noise.normalNoise(x, y, 2000f, 3f));
		temp += (Noise.normalNoise(x, y, 1000f, 3f));
		
		temp /= 6.0;
		
		return (float)temp;
	}
}
