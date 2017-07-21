package io.anuke.koru.entities.types;

import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.renderers.ParticleRenderer;
import io.anuke.koru.traits.ParticleTrait;
import io.anuke.koru.traits.PositionComponent;
import io.anuke.koru.traits.RenderComponent;

public class Particle extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new RenderComponent(new ParticleRenderer()), new ParticleTrait());
	}

}
