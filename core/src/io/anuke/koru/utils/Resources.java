package io.anuke.koru.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.items.BlockRecipes;
import io.anuke.koru.world.materials.Materials;
import io.anuke.koru.world.materials.StructMaterials;
import io.anuke.ucore.core.DrawContext;

public class Resources{
	private static Json json;
	private static ObjectMap<String, ParticleEffectPool> particles = new ObjectMap<String, ParticleEffectPool>();
	
	public static void loadMaterials(){
		Materials.load();
		StructMaterials.load();
		BlockRecipes.load();
	}
	
	public static void loadParticle(String name){
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/" + name), DrawContext.atlas);
		particles.put(name, new ParticleEffectPool(effect, 5, 30));
	}
	
	public static PooledEffect particle(String name){
		return particles.get(name).obtain();
	}
	
	public static BitmapFont font(){
		return DrawContext.font;
	}
	
	public static BitmapFont font2(){
		return DrawContext.skin.getFont("pixel-font-noborder");
	}
	
	public static BitmapFont font3(){
		return DrawContext.skin.getFont("pixel-font");
	}
	
	public static BitmapFont font(String name){
		return DrawContext.skin.getFont(name);
	}
	
	public static Json getJson(){
		if(json == null) json = new Json();
		return json;
	}
}
