package net.pixelstatic.koru.renderers;

import net.pixelstatic.koru.components.ParticleComponent;
import net.pixelstatic.koru.modules.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
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
	
	private void setVelocity(ParticleEffect particle, float v, float g){
		ScaledNumericValue velocity = particle.getEmitters().first().getVelocity();
		ScaledNumericValue gravity = particle.getEmitters().first().getGravity();
		velocity.setHigh(velocity.getHighMin() * v, velocity.getHighMax() * v);
		gravity.setHigh(gravity.getHighMin() * g, gravity.getHighMax() * g);
		velocity.setLow(velocity.getLowMin() * v, velocity.getLowMax() * v);
		gravity.setLow(gravity.getLowMin() * g, gravity.getLowMax() * g);
	}

	@Override
	protected void render(){
		//render.layers.update(entity.getX(), entity.getY());
		//if(render.layers.layer("particle").particle.isComplete()){
		//	entity.removeSelf();
		//	render.layers.layer("particle").particle.free();
		//}
	}

	@Override
	protected void initRender(){
		ParticleComponent component = entity.mapComponent(ParticleComponent.class);
		PooledEffect particle = getParticle(entity.mapComponent(ParticleComponent.class).name);
		setVelocity(particle, component.velocity, component.gravity);
		float[] value = particle.getEmitters().first().getTint().getColors();
		Color start = component.getStartColor();
		Color end = component.getEndColor();

		value[0] = start.r;
		value[1] = start.g;
		value[2] = start.b;
		value[3] = end.r;
		value[4] = end.g;
		value[5] = end.b;

		//render.layers.layer("particle").setParticle(particle);
	}

}
