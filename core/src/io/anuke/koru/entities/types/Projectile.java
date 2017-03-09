package io.anuke.koru.entities.types;

import io.anuke.koru.components.*;
import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.renderers.ProjectileRenderer;

public class Projectile extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new RenderComponent(new ProjectileRenderer()),
				new ColliderComponent(), new ProjectileComponent(),
				new FadeComponent(), new DestroyOnTerrainHitComponent(), new DamageComponent());
	}
	
	@Override
	public void init(KoruEntity entity){
		entity.collider().collider.trigger = true;
		entity.collider().grounded = false;
	}
	
	@Override
	public boolean collide(KoruEntity entity, KoruEntity other){
		return entity.get(DamageComponent.class).source != other.getID() 
				&& !entity.get(ProjectileComponent.class).hit.contains(other.getID());
	}

}
