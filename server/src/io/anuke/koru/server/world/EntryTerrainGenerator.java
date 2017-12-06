package io.anuke.koru.server.world;

import static io.anuke.ucore.util.Mathf.clamp;

import com.badlogic.gdx.utils.Array;

import io.anuke.koru.world.Generator;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.noise.RidgedPerlin;
import io.anuke.ucore.noise.Simplex;
import io.anuke.ucore.util.Mathf;

public class EntryTerrainGenerator implements Generator{
	Simplex tnoise = new Simplex();
	Simplex enoise = new Simplex();
	Simplex cnoise = new Simplex();
	Simplex rnoise = new Simplex();
	RidgedPerlin ridge = new RidgedPerlin(2, 1, 1f);
	RidgedPerlin mountains = new RidgedPerlin(2, 10, 0.8f);

	float negationOffset = 9999999;
	float riverThreshold = 0.616f;

	Array<Entry> entries = Array.with(
		new Entry(Materials.pinetree, 0.04f, 0.1f, 0.25f, 0.7f, 0.2f), 
		new Entry(Materials.pinesapling, 0.01f, 0.1f, 0.1f, 0.7f, 0.1f), 
		new Entry(Materials.pinecones, 0.01f, 0.1f, 0.1f, 0.7f, 0.1f), 
		new Entry(Materials.oaktree, 0.02f, 0.3f, 0.15f, 0.2f, 0.25f),
		new Entry(Materials.sapling, 0.01f, 0.1f, 0.15f, 0.2f, 0.25f),
		new Entry(Materials.birch, 0.025f, 0.1f, 0.1f, 0.5f, 0.28f),
		new Entry(Materials.floweryellow, 0.9f, 0.5f, 0.017f, 0.4f, 0.01f, 0.8f),
		new Entry(Materials.flowerblue, 0.9f, 0.2f, 0.015f, 0.6f, 0.01f, 0.8f),
		new Entry(Materials.flowerred, 0.9f, 0.4f, 0.015f, 0.4f, 0.01f, 0.8f),
		new Entry(Materials.flowerpurple, 0.9f){{
			river = riverThreshold -  0.01f;
			riverRange = 0.0098f;
			riverOffset = -0.34f;
			offset = 0.2f;
			temp = 0.2f;
			tempRange = 0.04f;
			elev = 0.2f;
			elevRange = 0.04f;
		}},

		new Entry(Materials.mushy, 0.007f, 0.3f, 0.15f, 0.2f, 0.24f),

		new Entry(Materials.grassblock, 1f, 0.35f, 0.04f, 0.51f, 0.048f, 1f), 
		new Entry(Materials.shortgrassblock, 0.92f, 0.35f, 0.04f, 0.51f, 0.053f, 1f),
		
		new Entry(Materials.grassblock, 1f, 0.65f, 0.03f, 0.5f, 0.02f, 1f), 
		new Entry(Materials.shortgrassblock, 0.92f, 0.65f, 0.03f, 0.5f, 0.03f, 1f),
		
		new Entry(Materials.olive, 0.003f, 0.55f, 0.13f, 0.0f, 0.3f),
		new Entry(Materials.acacia, 0.006f, 0.66f, 0.13f, 0.4f, 0.4f), 
		new Entry(Materials.deadtree, 0.004f, 0.64f, 0.1f, 0.5f, 0.4f), 
		new Entry(Materials.burnedtree, 0.003f, 0.9f, 0.1f, 0.5f, 0.4f), 
		new Entry(Materials.drybush, 0.015f, 0.66f, 0.12f, 0.5f, 0.4f), 
		new Entry(Materials.bush, 0.02f, 0.47f, 0.06f, 0.6f, 0.35f), 
		new Entry(Materials.rock, 0.007f, 0.6f, 0.2f, 0.8f, 0.4f),
		new Entry(Materials.rock, 0.004f, 0.1f, 0.1f, 0.8f, 0.4f),

		new Entry(Materials.willowtree, 0.01f, riverThreshold - 0.002f, 0.002f){{
			temp = 0.2f;
			tempRange = 0.3f;
		}},
		new Entry(Materials.droopy, 0.015f, riverThreshold - 0.004f, 0.004f){{
			temp = 0.0f;
			tempRange = 0.3f;
		}},
		new Entry(Materials.cherry, 0.005f, riverThreshold - 0.008f, 0.003f){{
			temp = 0.0f;
			tempRange = 0.3f;
			elev = 0.5f;
			elevRange = 0.1f;
		}},
		new Entry(Materials.rock, 0.015f, riverThreshold + 0.005f, 0.005f)
	);

	Material[][] floors = { 
		{Materials.darkgrass, Materials.darkgrass, Materials.darkgrass, Materials.forestgrass, 
					Materials.forestgrass, Materials.stone, Materials.ice},

		{Materials.darkgrass, Materials.darkgrass, Materials.darkgrass, Materials.forestgrass, 
					Materials.forestgrass, Materials.stone, Materials.ice},

		{Materials.darkgrass, Materials.forestgrass, Materials.forestgrass, Materials.forestgrass, 
					Materials.drygrass, Materials.stone, Materials.ice},

		{Materials.forestgrass, Materials.drygrass, Materials.drygrass, Materials.drygrass, 
					Materials.burntgrass, Materials.stone, Materials.ice},

		{Materials.drygrass, Materials.burntgrass, Materials.burntgrass, Materials.burntgrass, 
					Materials.sand, Materials.sand, Materials.stone} 
	};

	public EntryTerrainGenerator() {
		tnoise.setSeed(Mathf.random(99999));
		enoise.setSeed(Mathf.random(99999));
		rnoise.setSeed(Mathf.random(99999));
		cnoise.setSeed(Mathf.random(99999));
	}

	@Override
	public Tile generate(int x, int y){
		Tile tile = new Tile(Materials.air, Materials.air);
		tile.x = x;
		tile.y = y;

		setTile(tile, x, y);

		return tile;
	}

	void setTile(Tile tile, int x, int y){
		float scale = 500f;
		
		float nscl = 0.007f;
		
		float mountainScale = 0.2f;
		
		float river = (float)((ridge.getValue(x, y + 100, 1f / 2000f) + 1f)
				+ rnoise.octaveNoise2D(3, 0.5f, 1f/50f, x + negationOffset, y + negationOffset) * nscl) / 2f;
		
		float mnoise = (float)((mountains.getValue(x, y + 100, 1f / (scale * 1.5f)) + 1f)
				+ rnoise.octaveNoise2D(4, 0.5f, 1f/80f, x + negationOffset, y + negationOffset) * 0.00001f);
		
		float cavenoise = Mathf.clamp((float) (cnoise.octaveNoise2D(8, 0.55, 1 / (scale + 400f), 
				x + negationOffset, y + negationOffset)), 0f, 0.99f);
		
		float temp = Mathf.clamp((float) (tnoise.octaveNoise2D(12, 0.63, 1 / scale, x + negationOffset, y + negationOffset))
				- river / 2f, 0f, 0.99f);
		
		float elev = Mathf.clamp((float)  ((1f - mountainScale) *(enoise.octaveNoise2D(12, 0.63, 1 / (scale * 1.2f), x + negationOffset, y 
				+ negationOffset) - river / 5f)) + mnoise* mountainScale, 0f, 0.99f);
		

		Material wall = Materials.air;
		Material floor = Materials.air;

		floor = floors[(int) (temp * floors.length)][(int) (elev * floors[0].length)];

		for(int i = 0; i < entries.size; i++){
			if(entries.get(i).eval(elev, temp, river)){
				wall = entries.get(i).material;
				break;
			}
		}
		
		float cavet = 0.64f;
		if(cavenoise > cavet){
			wall = Materials.stoneblock;
			floor = Materials.stone;
			tile.setLight(clamp(1f - (cavenoise - cavet) * 40f));
		}else if(cavenoise > cavet - 0.02){
			floor = Materials.stone;
		}
		

		if(river > 0.616f){
			Material before = wall;
			
			wall = Materials.air;

			if(river > riverThreshold + 0.005f)
				floor = Materials.deepwater;
			else if(river > riverThreshold + 0.003f)
				floor = Materials.water;
			else{
				wall = before;
				floor = Materials.stone;
			}
		}

		tile.setWall(wall);
		tile.setMaterial(floor);
	}

	class Entry{
		Material material;
		float temp, tempRange;
		float elev, elevRange;
		float river = 0f, riverRange = riverThreshold;
		//chance offset. for offset = 0, things at eligibility 0.1 will have 1/10th the chance of spawning
		//for offset 1, things will always have the same chance of spawning while they are eligible
		float offset = 0.5f, riverOffset = 0.85f;
		float chance;

		public Entry(Material material, float amount, float temp, float tempRange, float elev, float elevRange, float falloff) {
			this.material = material;
			this.temp = temp;
			this.tempRange = tempRange;
			this.elev = elev;
			this.elevRange = elevRange;
			this.chance = amount;
			this.offset = falloff;
		}

		public Entry(Material material, float amount, float temp, float tempRange, float elev, float elevRange) {
			this(material, amount, temp, tempRange, elev, elevRange, 0.5f);
		}

		public Entry(Material material, float chance, float river, float riverRange) {
			this.material = material;
			this.chance = chance;
			this.river = river;
			this.riverRange = riverRange;
			this.temp = 0.5f;
			this.elev = 0.5f;
			this.tempRange = 0.5f;
			this.elevRange = 0.5f;
			this.offset = 1f;
		}
		
		public Entry(Material material, float chance){
			this.material = material;
			this.chance = chance;
		}

		boolean eval(float celev, float ctemp, float criver){

			return Mathf.chance(Mathf.clamp(Math.min(Math.min(
					weight(celev, elev, elevRange, offset), 
					weight(ctemp, temp, tempRange, offset)),
					weight(criver, river, riverRange, riverOffset)
			)) * chance);
		}
	}
	
	float weight(float value, float target, float range, float offset){
		float out = 1f - Math.abs(value - target) / range;
		
		if(out <= 0f)
			out = -1f;
		
		out += offset;
		
		return out;
	}

}
