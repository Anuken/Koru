package io.anuke.koru.graphics.lsystems;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.graphics.Evaluators;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.lsystem.EvolutionData;
import io.anuke.ucore.lsystem.Evolver;
import io.anuke.ucore.lsystem.LSystemData;
import io.anuke.ucore.util.Mathf;

public class LSystems{
	public static final int variants = 8;
	private static final Evolver evolver = new Evolver();
	private static final Color leafColor = Color.valueOf("366d2d");
	private static final Color trunkColor = Color.valueOf("96572a");
	private static final Array<KoruLSystem> systems = new Array<>();
	
	private static final String shrub = "{X=[[+[-XF][X]-XXFF+]-[FF]X][F-]-F, F=F}";
	private static final String olive = "{X=[+[+[FX-[F+[FX]X]-XXF]]]XX-F, F=F}";
	private static final String cherry = "{X=[F[--FX+[+FFX][XX--XF]]X[X]]X+F, F=F}";
	private static final String birch = "{X=[F[+[X+X+]FXX-[[X+X-][X++]X]]-F[X]X]F, F=F}";
	private static final String birch2 = "{X=X[++[[---F[X][X+X-F++++]-X]+X]X]XF, F=F}";
	private static final String bush = "{X=[[X-[-XF-F]+F++X]XXX+]F, F=F}";
	private static final String twisty = "{X=-XF+[[+++X[[X]+X]-F[-X]]-F]F, F=F}";
	private static final String droopy = "{X=+X-[[X+[[F+X+[F][FX]+FFX-]][F]]]XF, F=F}";
	private static final String acacia = "{X=[F+[X--F[[X]-X][[X]]+X]XF]F, F=F}";
	private static final String acacia2 = "{X=[-FF[[X[[X][FX+[-[F]XXFXX]]-]]]X-F]+F, F=F}";
	private static final String branchy = "{X=+F[XF[+X-[[[[X]X]-X]+X[F]F]X]]--[FFX-]F, F=F}";
	private static final String sapling = "{X=[F+X+[FX[XX-F]+[F][--]+XF]-]-F, F=F}";
	private static final String deadbush = "{X=-[++F+X--FFX[-X+F-[[F]X-]++[[X+]X[FF]F]-]F]F, F=F}";
	
	private static ObjectMap<String, KoruLSystem[]> map = new ObjectMap<>();
	
	public static KoruLSystem[] test;
	
	public static void generate(){
		for(KoruLSystem system : systems){
			system.dispose();
		}
		systems.clear();
		map.clear();
		
		EvolutionData bush = new EvolutionData();
		bush.eval = Evaluators.leafcount;
		bush.generations = 20;
		bush.swayscale = 18f;
		bush.swayphase = 4f;
		bush.swayspace = 1f;
		bush.changeSpace = false;
		bush.defaultSpace = 17f;
		bush.start = bush.end = Hue.mix(Color.BROWN, Color.WHITE, 0.1f);
		bush.thickness = 2f;
		bush.defaultRules.put('F', "F");
		
		test = evolve(bush, test);
		map.put("ltest", test);
		
		evolveType("birch", birch, Color.valueOf("b8b1ae"), Color.valueOf("558f4b"), 1);
		evolveType("olive", olive, Color.valueOf("7e5231"), Color.valueOf("558739"));
		evolveType("shrub", shrub, trunkColor, leafColor, 2);
		evolveType("cherry", cherry, Color.valueOf("825433"), Color.valueOf("e7a4d8")/*, Color.WHITE*/);
		evolveType("acacia", acacia2, Color.valueOf("775236"), Color.valueOf("5d963e"), 0);
		evolveType("sapling", sapling, trunkColor, leafColor);
		evolveType("deadbush", deadbush, Color.valueOf("7c4c3d"), Color.valueOf("7c9b39"));
		evolveType("droopy", droopy, Color.valueOf("5a3628"), Color.valueOf("386a3e"));
		set("droopy", k -> k.getData().thickness = 2f);
	}
	
	private static void set(String type, Consumer<KoruLSystem> cons){
		for(KoruLSystem s : map.get(type)){
			cons.accept(s);
		}
	}
	
	public static KoruLSystem[] getSystems(String name){
		return map.get(name);
	}
	
	public static void cacheAll(){
		for(KoruLSystem system : systems){
			if(!system.isDrawn())
				system.draw();
		}
	}
	
	private static void evolveType(String name, String instructions, Color trunk, Color leaves){
		evolveType(name, instructions, trunk, leaves, 0);
	}
	
	private static void evolveType(String name, String instructions, Color trunk, Color leaves, Color leavesTo){
		evolveType(name, instructions, trunk, leaves, 0);
		for(KoruLSystem k : map.get(name)){
			k.leafColorEnd = leavesTo;
		}
	}
	
	private static void evolveType(String name, String instructions, Color trunk, Color leaves, int iterations){
		KoruLSystem[] systems = new KoruLSystem[variants];
		if(!map.containsKey(name)){
			map.put(name, systems);
		}
		
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
			data.swayphase = 23f;
			data.space = 17f  * Mathf.sign(Mathf.chance(0.5)) + Mathf.range(3f);
			data.thickness = 2f;
			data.iterations = 3;
			data.iterations += Mathf.random(iterations);
			data.len = 4f + Mathf.range(0.2f);
			
			KoruLSystem system = new KoruLSystem(data);
			systems[i] = system;
			system.leafColor = leaves;
			system.trunkHeight = Mathf.range(2f);
			LSystems.systems.add(system);
		}
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
