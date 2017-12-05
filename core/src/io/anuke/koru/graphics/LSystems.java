package io.anuke.koru.graphics;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.lsystem.EvolutionData;
import io.anuke.ucore.lsystem.Evolver;
import io.anuke.ucore.lsystem.LSystemData;
import io.anuke.ucore.util.Mathf;

public class LSystems{
	public static final int variants = 16;
	private static final Evolver evolver = new Evolver();
	private static final Color leafColor = Color.valueOf("366d2d");
	private static final Color trunkColor = Color.valueOf("96572a");
	private static final Array<KoruLSystem> systems = new Array<>();
	
	private static final String shrub = "{X=[[+[-XF][X]-XXFF+]-[FF]X][F-]-F, F=F}";
	private static final String olive = "{X=[+[+[FX-[F+[FX]X]-XXF]]]XX-F, F=F}";
	private static final String cherry = "{X=[+[+[FX-[F+[FX]X]-XXF]]]XX-F, F=F}";
	private static final String birch = "{X=[F[+[X+X+]FXX-[[X+X-][X++]X]]-F[X]X]F, F=F}";
	private static final String bush = "{X=[[X-[-XF-F]+F++X]XXX+]F, F=F}";
	private static final String twisty = "{X=-XF+[[+++X[[X]+X]-F[-X]]-F]F, F=F}";
	private static final String droopy = "{X=+X-[[X+[[F+X+[F][FX]+FFX-]][F]]]XF, F=F}";
	private static final String acacia = "{X=[F+[X--F[[X]-X][[X]]+X]XF]F, F=F}";
	
	public static KoruLSystem[] test;
	public static KoruLSystem[] birches;
	public static KoruLSystem[] olives;
	public static KoruLSystem[] bushes;
	
	public static void generate(){
		for(KoruLSystem system : systems){
			system.dispose();
		}
		systems.clear();
		
		EvolutionData bush = new EvolutionData();
		bush.eval = Evaluators.leafcount;
		bush.generations = 10;
		bush.swayscale = 18f;
		bush.swayphase = 4f;
		bush.swayspace = 1f;
		bush.changeSpace = false;
		bush.defaultSpace = 17f;
		bush.start = bush.end = Hue.mix(Color.BROWN, Color.WHITE, 0.1f);
		bush.thickness = 2f;
		bush.defaultRules.put('F', "F");
		
		test = evolve(bush, test);
		
		birches = evolveType(birches, birch, Color.valueOf("b8b1ae"), Color.valueOf("558f4b"));
		olives = evolveType(olives, olive, Color.valueOf("7e5231"), Color.valueOf("4f8c2d"));
		bushes = evolveType(bushes, shrub, trunkColor, leafColor);
	}
	
	public static void cacheAll(){
		for(KoruLSystem system : systems){
			if(!system.isDrawn())
				system.draw();
		}
	}
	
	private static KoruLSystem[] evolveType(KoruLSystem[] out, String instructions, Color trunk, Color leaves){
		KoruLSystem[] systems = new KoruLSystem[variants];
		instructions = instructions.substring(1, instructions.length()-1);
		HashMap<Character, String> rules = new HashMap<>();
		for(String instr : instructions.split(", ")){
			String[] split = instr.split("=");
			rules.put(split[0].charAt(0), split[1]);
		}
		
		for(int i = 0; i < variants; i ++){
			LSystemData data = new LSystemData("FX", rules, 0, 0, 0, 0, 0, 0, 0, null, null);
			data.start = data.end = trunk;
			data.swayscl = 2f;
			data.swayspace = 4f;
			data.swayphase = 18f;
			data.space = 17f  * Mathf.sign(Mathf.chance(0.5)) + Mathf.range(3f);
			data.thickness = 2f;
			data.iterations = 3;
			data.len = 4f + Mathf.range(0.2f);
			
			KoruLSystem system = new KoruLSystem(data);
			system.leafColor = leaves;
			systems[i] = system;
			LSystems.systems.add(system);
			
			if(out != null){
				out[i] = systems[i];
			}
		}
		
		if(out != null){
			return out;
		}
		
		return systems;
	}
	
	private static KoruLSystem[] evolve(EvolutionData data, KoruLSystem[] out){
		LSystemData[] result = evolver.evolve(data, variants);
		KoruLSystem[] systems = new KoruLSystem[variants];
		
		for(LSystemData d : result){
			for(char key : d.rules.keySet()){
				d.rules.put(key, fix(d.rules.get(key)));
			}
		}
		
		for(int i = 0; i < variants; i ++){
			systems[i] = new KoruLSystem(result[i]);
			systems[i].leafColor = leafColor;
			LSystems.systems.add(systems[i]);
			if(out != null){
				out[i] = systems[i];
			}
		}
		
		if(out != null){
			return out;
		}
		
		return systems;
	}
	
	private static String fix(String instruction){
		//int start = instruction.length();
		int last = instruction.length() + 1;
		while(last != instruction.length()){
			last = instruction.length();
			instruction = instruction.replace("[]", "");
			instruction = instruction.replace("[-]", "");
			instruction = instruction.replace("[+]", "");
			instruction = instruction.replace("+-", "");
			instruction = instruction.replace("-+", "");
		}
		return instruction;
	}
}
