package io.anuke.koru.graphics;

import com.badlogic.gdx.utils.Pools;

import io.anuke.ucore.facet.*;

public class RenderPool{
	
	public static KoruRenderable get(String region){
		return Pools.obtain(KoruRenderable.class).region(region);
	}
	
	public static KoruRenderable shadow(String region){
		return Pools.obtain(KoruRenderable.class).region(region).shadow();
	}
	
	public static TextFacet text(){
		return Pools.obtain(TextFacet.class);
	}

	public static SpriteFacet light(){
		return Pools.obtain(KoruRenderable.class);
	}
	
	public static ParticleFacet particle(){
		return Pools.obtain(ParticleFacet.class);
	}
	
	public static void free(Facet r){
		if(r.getClass().isAnonymousClass()) return;
		
		Pools.free(r);
	}
}
