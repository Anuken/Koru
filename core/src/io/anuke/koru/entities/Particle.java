package io.anuke.koru.entities;

import io.anuke.koru.components.ParticleComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.renderers.ParticleRenderer;

public class Particle extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new RenderComponent(new ParticleRenderer()), new ParticleComponent());
	}

}
