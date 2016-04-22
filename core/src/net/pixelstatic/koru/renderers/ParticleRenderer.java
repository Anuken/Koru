package net.pixelstatic.koru.renderers;

import net.pixelstatic.koru.modules.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.ObjectMap;

public class ParticleRenderer extends EntityRenderer{
	private static ObjectMap<String, ParticleEffectPool> pools = new ObjectMap<String, ParticleEffectPool>();
	private static Renderer renderer;
	
	public static void loadParticles(Renderer render){
		renderer = render;
		
		particle("spark");
	}
	
	private static void particle(String name){
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/" + name), renderer.atlas());
		pools.put(name, new ParticleEffectPool(effect, 5, 30));
	}
	
	private static PooledEffect getParticle(String name){
		return pools.get(name).obtain();
	}
	
	@Override
	protected void render(){
		render.layers.update(entity.getX(), entity.getY());
		if(render.layers.layer("particle").particle.isComplete()){
			entity.removeSelf();
			render.layers.layer("particle").particle.free();
		}
	}

	@Override
	protected void initRender(){
		render.layers.layer("particle").setParticle(getParticle("spark"));
	}

}
