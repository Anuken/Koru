package io.anuke.koru.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;

import io.anuke.koru.traits.ParticleTrait;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.renderables.ParticleRenderable;

public class ParticleRenderer extends EntityRenderer{
	
	
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
		ParticleRenderable r = (ParticleRenderable)render.list.first();
		
		r.set(entity.getX(), entity.getY());
		
		if(r.effect.isComplete()){
			entity.remove();
			r.effect.free();
		}
	}

	@Override
	protected void init(){
		ParticleTrait component = entity.get(ParticleTrait.class);
		PooledEffect particle = Resources.particle(entity.get(ParticleTrait.class).name);
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
		
		render.list.add(new ParticleRenderable(particle));
	}

}
