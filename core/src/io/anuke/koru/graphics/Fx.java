package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects.Effect;
import io.anuke.ucore.util.Angles;

public class Fx{
	public static final Effect

	blockbreak = new Effect(30, e -> {
		Draw.color(e.color);
		Angles.randLenVectors(e.id, 10, 1f + 17f * e.powfract(), (x, y) -> {
			float rad = 1f + e.fract() * 8f;
			Draw.rect("circle", x + e.x, y + e.y, rad, rad);
		});
		Draw.color();
	}),

	blockplace = new Effect(20, e -> {
		Draw.color(Color.TAN);
		Angles.randVectors(e.id, 6, 1f + 14f * e.powfract(), (x, y) -> {
			float rad = 1f + e.fract() * 4f;
			Draw.rect("circle", x + e.x, y + e.y, rad, rad);
		});
		Draw.color();
	}),

	blockparticle = new Effect(14, e -> {
		Draw.color(e.color);
		Angles.randLenVectors(e.id, 4, 1f + 14f * e.ifract(), (x, y) -> {
			float rad = e.fract() * 5f;
			Draw.rect("circle", x + e.x, y + e.y, rad, rad);
		});
		Draw.color();
	}),

	itempickup = new Effect(9, e -> {
		Draw.color(Color.LIGHT_GRAY);
		Angles.randVectors(e.id, 4, 8f * e.fract(), (x, y) -> {
			float rad = e.sfract() * 4f;
			Draw.rect("circle", x + e.x, y + e.y, rad, rad);
		});
		Draw.color();
	});
}
