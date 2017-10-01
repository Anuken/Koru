package io.anuke.koru.graphics;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.koru.graphics.Shaders.Water;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.facet.FacetLayer;

public class KoruFacetLayers{
	public static final float waterLayer = 2;
	
	public static final FacetLayer
	
	water = new FacetLayer("water", waterLayer, 0){
		
		{
			Draw.addSurface("reflection", Core.cameraScale, 3);
		}
		
		@Override
		public boolean layerEquals(float f){
			return MathUtils.isEqual(f, layer, 2f);
		}
		
		@Override
		public void end(){
			Draw.color();
			
			Draw.shader(Water.class);
			Draw.flushSurface();
			Draw.shader();
		}
	};
}
