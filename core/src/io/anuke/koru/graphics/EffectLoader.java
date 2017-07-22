package io.anuke.koru.graphics;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;

public class EffectLoader{
	
	public static void load(){
		Effects.create("blockbreak", 20, e->{
			Draw.color(e.color);
			Draw.square(e.x, e.y, 8f*e.ifract());
			Draw.color();
		});
		
		Effects.create("blockparticle", 10, e->{
			Draw.color(e.color);
			Draw.square(e.x, e.y, 3f*e.ifract());
			Draw.color();
		});
	}
}
