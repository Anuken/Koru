package io.anuke.koru.graphics;

import com.badlogic.gdx.utils.Pools;

import io.anuke.ucore.facet.Facet;

public class FacetPool{
	
	public static KoruFacet get(String region){
		return Pools.obtain(KoruFacet.class).region(region);
	}
	
	public static void free(Facet r){
		if(r.getClass().isAnonymousClass()) return;
		
		Pools.free(r);
	}
}
