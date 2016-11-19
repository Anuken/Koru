package io.anuke.koru;

import static io.anuke.ucore.util.UCore.clamp;

import io.anuke.koru.world.Generator;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.noise.RidgedPerlin;
import io.anuke.ucore.noise.VoroniNoise;
import io.anuke.ucore.util.UCore;

public class TerrainGenerator implements Generator{
	final float scale = 1f;
	VoroniNoise tnoise = new VoroniNoise(0,(short) 0);
	VoroniNoise enoise = new VoroniNoise(10,(short) 0);
	RidgedPerlin per = new RidgedPerlin(2, 1, 0.4f);
	
	{
		enoise.setUseDistance(true);
		tnoise.setUseDistance(true);
	}

	@Override
	public Tile generate(int x, int y){
		x += 99999;
		y += 99999;

		Tile tile = new Tile();
		float e = getElevation(x, y);
		float se = (smoothEl(x,y)+0.4f) / 0.82f;
		float riv = per.getValue(x, y, 0.0005f);
		
		float t = getTemperature(x, y);
		
		if(riv > 0.23f){
			tile.setMaterial(Materials.riveredge);
			if(riv > 0.236) tile.setMaterial(Materials.water);
			if(riv > 0.244) tile.setMaterial(Materials.deepwater);
			
		}else if(e > 0.85){
			tile.setMaterial(Materials.ice);

			if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
			if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.blackrock1, 4));
			
		}else if(e > 0.76){
			tile.setMaterial(Materials.stone);
			
			if(Math.random() < 0.05) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
			
		}else if (se > 0.078){
			if(t < 0.62){
				if(Math.random() < 1){
					if(Math.random() < 0.1 && e > 0.25f)
						tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
					if(Math.random() < 0.03)
						tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));

					if(Math.random() < 0.13 && e < 0.4f)
						tile.setBlockMaterial(Materials.next(Materials.wheatgrass1, 3));
					if(Math.random() < 0.04 && e < 0.5f)
						tile.setBlockMaterial(Materials.next(Materials.wheatgrass1, 3));

					if(Math.random() < 0.02 && e < 0.35f && e > 0.12f)
						tile.setBlockMaterial(Materials.next(Materials.bush1, 3));

					if(Math.random() < 0.03 * e)
						tile.setBlockMaterial(Materials.next(Materials.fern1, 3));
					if(Math.random() < 0.01 * e)
						tile.setBlockMaterial(Materials.next(Materials.koru1, 3));
				}
				if(e < 0.4 && e > 0.1 && t < 0.6 && t > 0.41){
					if(Noise.normalNoise(x, y, 120, 13) + Noise.normalNoise(x, y, 5, 4) > 4)
						tile.setMaterial(Materials.shortgrassblock);

					if(Noise.normalNoise(x, y, 120, 13) + Noise.normalNoise(x, y, 5, 4) > 4.5)
						tile.setMaterial(Materials.grassblock);
				}
				if(e > 0.36f && e < 0.53f && Noise.normalNoise(x, y, 500, 30) + Noise.normalNoise(x, y, 9, 4) > 3){
					if(rand() < br(0.12f, e)){
						if(rand() < 0.026)
							tile.setMaterial(Materials.next(Materials.oaktree1, 6));
						if(rand() < 0.02)
							tile.setMaterial(Materials.next(Materials.mushy1, 8));
						if(Math.random() < 0.05)
							tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
					}
				}
				if((e > 0.5f && Math.random() < clamp((e - 0.5f) * 3f) / 15f)){
					tile.setMaterial(Materials.next(Materials.pinetree1, 4));
					if(Math.random() < 0.02)
						tile.setMaterial(Materials.next(Materials.mossyrock1, 2));

					if(Math.random() < 0.002)
						tile.setBlockMaterial(Materials.pinesapling);
				}
				
				
				if(t < 0.4){
					tile.setMaterial(Materials.darkgrass);
				}else{
					tile.setMaterial(Materials.grass);
				}
			}else if(t < 0.8){
				tile.setMaterial(Materials.burntgrass);
				if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.drybush1, 3));
				if(Math.random() < 0.003) tile.setBlockMaterial(Materials.next(Materials.deadtree1, 4));
				
				if(Math.random() < 0.01) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
				if(Math.random() < 0.1) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
			
			}else if(t < 0.83){
				tile.setMaterial(Materials.burntgrass2);
				if(Math.random() < 0.04) tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
				if(Math.random() < 0.001) tile.setBlockMaterial(Materials.next(Materials.deadtree1, 4));
				if(Math.random() < 0.001) tile.setBlockMaterial(Materials.next(Materials.burnedtree1, 4));
			}else{
				tile.setMaterial(Materials.sand);
				if(Math.random() < 0.02) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
				
			}
			
		}else{
			if(t < 0.3){
				tile.setMaterial(Materials.darkgrass);
			}else{
				tile.setMaterial(Materials.grass);
			}
			if(se < 0.063f){ 
				tile.setMaterial(Materials.water);
			}else if(se < 0.066){
				tile.setMaterial(Materials.riveredge);
				if(Math.random() < 0.01) tile.setMaterial(Materials.next(Materials.rock1, 4));
				if(Math.random() < 0.003) tile.setMaterial(Materials.next(Materials.mossyrock1, 2));
			}else{
				if(Math.random() < 0.1) tile.setMaterial(Materials.next(Materials.wheatgrass1, 3));
				if(Math.random() < 0.1) tile.setMaterial(Materials.next(Materials.tallgrass1, 3));
				if(Math.random() < 0.006 && se > 0.069) tile.setMaterial(Materials.next(Materials.willowtree1, 4));
				
				
			}
		}

		/*
		if(t < 0.7 && t > 0.4 && e < 0.15){
			tile.setMaterial(Materials.burntgrass);
			if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.drybush1, 3));
			if(Math.random() < 0.003) tile.setBlockMaterial(Materials.next(Materials.deadtree1, 4));
			
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
					if(Math.random() < 0.001) tile.setBlockMaterial(Materials.next(Materials.deadtree1, 4));
					if(Math.random() < 0.001) tile.setBlockMaterial(Materials.next(Materials.burnedtree1, 4));
					
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
				if(Math.random() < 0.05) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
				
			}else if(e < 1f){
				tile.setMaterial(Materials.ice);
				
				if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.rock1, 4));
				if(Math.random() < 0.03) tile.setBlockMaterial(Materials.next(Materials.blackrock1, 4));
				
			}

			if(e < 0.35f || (e < 0.4f && Math.random() < (0.45f - e) * 200)){
				if(Math.random() < 0.1 && e > 0.03f)
					tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));
				if(Math.random() < 0.03)
					tile.setBlockMaterial(Materials.next(Materials.tallgrass1, 3));

				if(Math.random() < 0.13 && e < 0.1f)
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
				if(Math.random() < 0.02)tile.setMaterial(Materials.next(Materials.mossyrock1, 2));
				
				if(Math.random() < 0.002)
					tile.setBlockMaterial(Materials.pinesapling);
			}

			if(e < 0.1f && Noise.normalNoise(x, y, 120, 13) + Noise.normalNoise(x, y, 5, 4) > 4 && t < 0.3)
				tile.setMaterial(Materials.shortgrassblock);

			if(e < 0.1f && Noise.normalNoise(x, y, 120, 13) + Noise.normalNoise(x, y, 5, 4) > 4.5 && t < 0.3)
				tile.setMaterial(Materials.grassblock);
		}
	
	*/
		
		
		return tile;
	}
	
	float br(float e, float b){
		return clamp(1f-Math.abs(e-b));
	}
	
	public float getElevation(int x, int y){
		x += 999999;
		y += 999999;

		double elevation = 0.4f;
		float octave = 1200f*scale;
		
		elevation += smoothEl(x,y);
		elevation += (Noise.nnoise(x, y, octave/128, 0.125f));
		elevation += (Noise.nnoise(x, y, octave/32, 0.125f/4));
		elevation += enoise.noise(x, y, 1/1000.0)/3.4;
		
		elevation /= 0.84;
		
		elevation = UCore.clamp(elevation);

		return (float) elevation;

	}
	
	public float smoothEl(int x, int y){
		double elevation = 0f;
		float octave = 1200f*scale;
		
		elevation += (Noise.nnoise(x, y, octave, 1f));
		elevation += (Noise.nnoise(x, y, octave/2, 0.5f));
		elevation += (Noise.nnoise(x, y, octave/4, 0.25f));
		elevation += (Noise.nnoise(x, y, octave/8, 0.125f));
		elevation += (Noise.nnoise(x, y, octave/16, 0.125f/2));
		elevation += (Noise.nnoise(x, y, octave/32, 0.125f/4));
		elevation += (Noise.nnoise(x, y, octave/128, 0.125f/16));
		

		return (float) elevation;
	}

	public float getTemperature(int x, int y){
		x += 99999 * 2;
		y += 99999 * 2;

		double temp = 0.5f;
		float octave = 800f;
		
		temp += (Noise.nnoise(x, y, octave, 1f));
		temp += (Noise.nnoise(x, y, octave/2, 0.5f));
		temp += (Noise.nnoise(x, y, octave/4, 0.25f));
		temp += (Noise.nnoise(x, y, octave/8, 0.125f));
		//temp += (Noise.nnoise(x, y, octave/16, 0.25f));
		temp += (Noise.nnoise(x, y, octave/32, 0.125f));
		temp += (Noise.nnoise(x, y, octave/64, 0.125f/2));
		temp += (Noise.nnoise(x, y, octave/128, 0.125f/2));
		temp += tnoise.noise(x, y, 1/1000.0)/3.4;
		
		
		temp /= 1.05;
		
		
		temp = UCore.clamp(temp);

		return (float) temp;
	}


	double rand(){
		return Math.random();
	}
}
