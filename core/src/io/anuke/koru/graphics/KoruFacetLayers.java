package io.anuke.koru.graphics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.graphics.Shaders.Mix;
import io.anuke.koru.graphics.Shaders.Water;
import io.anuke.koru.world.materials.MaterialTypes.Wall;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.facet.*;

public class KoruFacetLayers{
	public static final float waterLayer = 2;
	
	public static final FacetLayer
	
	water = new FacetLayer("water", waterLayer, 0){
		
		{
			Draw.addSurface("reflection", Core.cameraScale, 3);
		}
		
		@Override
		public boolean acceptFacet(Facet facet){
			return layerEquals(facet.getLayer()) && facet.provider == Sorter.tile;
		}
		
		@Override
		public boolean layerEquals(float f){
			return MathUtils.isEqual(f, layer, 2f);
		}
		
		@Override
		public void end(){
			Draw.color();
			
			Draw.getShader(Mix.class).color.set(0x29619bff);
			Draw.shader(Mix.class);
			
			Array<Facet> facets = Facets.instance().getFacetArray();
			for(int i = 0; i < facets.size; i ++){
				Facet facet = facets.get(i);
				
				if(facet.provider != Sorter.object || facet instanceof ReflectionlessFacet){
					continue;
				}
				
				boolean iswall = false;
				int pheight = 0;
				int py = 0;
				
				if(facet instanceof KoruFacet){
					KoruFacet k = (KoruFacet)facet;
					
					if(!k.reflect) continue;
					
					if(k.material instanceof Wall){
						iswall = true;
						Wall wall = (Wall)k.material;
						pheight = k.sprite.getRegionHeight();
						py = k.sprite.getRegionY();
						k.sprite.setRegionY(k.sprite.getRegionY() + (k.sprite.getRegionHeight() - wall.height));
						k.sprite.setRegionHeight(wall.height);
						k.sprite.setSize(k.sprite.getWidth(), wall.height);
						
					}
				}
				
				Draw.end();
				
				
				//TODO facet lags behind in Y axis while moving?
				
				Draw.batch().getTransformMatrix().setToTranslation(0, facet.getLayer(), 0);
				Draw.batch().getTransformMatrix().scale(1f, -1f, 0);
				Draw.batch().getTransformMatrix().translate(0, -facet.getLayer(), 0);
				Draw.begin();
				facet.draw();
				
				if(iswall){
					KoruFacet k = (KoruFacet)facet;
					k.sprite.setRegionY(py);
					k.sprite.setRegionHeight(pheight);
					k.sprite.setSize(k.sprite.getWidth(), pheight);
				
				}
			}
			
			Draw.end();
			Draw.shader();
			
			Draw.batch().getTransformMatrix().setToTranslation(0, 0, 0);
			Draw.begin();
			
			Draw.shader(Water.class);
			Draw.flushSurface();
			Draw.shader();
		}
	};
}
