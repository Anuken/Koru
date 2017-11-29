package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.lsystem.EvolutionData;
import io.anuke.ucore.lsystem.Evolver;
import io.anuke.ucore.lsystem.LSystemData;

public class LSystems{
	public static final int variants = 6;
	private static final Evolver evolver = new Evolver();
	
	public static KoruLSystem[] brambles;
	
	static{
		generate();
	}
	
	public static void generate(){
		
		EvolutionData bush = new EvolutionData();
		bush.eval = Evaluators.leafcount;
		bush.generations = 10;
		bush.swayscale = 18f;
		bush.swayphase = 1f;
		bush.swayspace = 1f;
		bush.changeSpace = false;
		bush.defaultSpace = 17f;
		bush.start = bush.end = Hue.mix(Color.BROWN, Color.WHITE, 0.1f);
		bush.thickness = 2f;
		bush.defaultRules.put('F', "F");
		
		brambles = evolve(bush, brambles);
	}
	
	private static KoruLSystem[] evolve(EvolutionData data, KoruLSystem[] out){
		LSystemData[] result = evolver.evolve(data, variants);
		KoruLSystem[] systems = new KoruLSystem[variants];
		
		for(int i = 0; i < variants; i ++){
			systems[i] = new KoruLSystem(result[i]);
			if(out != null){
				out[i] = systems[i];
			}
		}
		
		if(out != null){
			return out;
		}
		
		return systems;
	}
}
