package io.anuke.koru.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.items.BlockRecipes;
import io.anuke.koru.modules.Renderer;
import io.anuke.koru.world.materials.Materials;
import io.anuke.koru.world.materials.StructMaterials;
import io.anuke.ucore.scene.style.Styles;

public class Resources{
	private static Json json;
	private static ObjectMap<String, ParticleEffectPool> particles = new ObjectMap<String, ParticleEffectPool>();
	private static Renderer rend;
	
	public static void loadMaterials(){
		Materials.load();
		StructMaterials.load();
		BlockRecipes.load();
	}
	
	public static void loadParticle(String name){
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/" + name), Resources.atlas());
		particles.put(name, new ParticleEffectPool(effect, 5, 30));
	}
	
	public static void set(Renderer r){
		rend = r;
	}
	
	public static PooledEffect particle(String name){
		return particles.get(name).obtain();
	}
	
	/**Side note: equivalent to Draw.region()*/
	public static AtlasRegion region(String name){
		return rend.atlas.findRegion(name);
	}
	
	public static boolean hasRegion(String name){
		return rend.atlas.hasRegion(name);
	}
	
	public static RepackableAtlas atlas(){
		return rend.atlas;
	}
	
	public static BitmapFont font(){
		return rend.font;
	}
	
	public static BitmapFont font2(){
		return Styles.styles.getFont("pixel-font-noborder");
	}
	
	public static BitmapFont font3(){
		return Styles.styles.getFont("pixel-font");
	}
	
	public static Json getJson(){
		if(json == null) json = new Json();
		return json;
	}
	
	public static Batch batch(){
		return rend.batch;
	}
}
