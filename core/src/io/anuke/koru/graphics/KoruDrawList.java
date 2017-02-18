package io.anuke.koru.graphics;

import io.anuke.koru.utils.Resources;
import io.anuke.ucore.spritesort.DrawLayer;
import io.anuke.ucore.spritesort.DrawPointerList;

public class KoruDrawList extends DrawPointerList{
	
	public void shadow(String name, float x, float y){
		add(Layers.shadow, DrawLayer.tile, (p)->{
			Draw.color(0, 0, 0);
			Draw.rect(("shadow"
					+ (int) (Resources.region(name).getRegionWidth() * 0.8f / 2f + 
							Math.pow(Resources.region(name).getRegionWidth(), 1.5f) / 200f) * 2), x, y);
			Draw.color();
		});
	}
}
