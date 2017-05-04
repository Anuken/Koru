package io.anuke.koru.graphics;

import com.badlogic.gdx.utils.Pools;

import io.anuke.ucore.renderables.*;

public class RenderPool{
	
	public static KoruRenderable get(String region){
		return Pools.obtain(KoruRenderable.class).region(region);
	}
	
	public static KoruRenderable shadow(String region){
		return Pools.obtain(KoruRenderable.class).region(region).shadow();
	}
	
	public static TextRenderable text(){
		return Pools.obtain(TextRenderable.class);
	}

	public static SpriteRenderable light(){
		return Pools.obtain(KoruRenderable.class);
	}
	
	public static ParticleRenderable particle(){
		return Pools.obtain(ParticleRenderable.class);
	}
	
	public static void free(Renderable r){
		if(r.getClass().isAnonymousClass()) return;
		
		Pools.free(r);
	}
}
