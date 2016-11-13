package io.anuke.koru.entities;

import io.anuke.koru.components.DamageComponent;
import io.anuke.koru.components.FadeComponent;
import io.anuke.koru.components.ProjectileComponent;

public enum ProjectileType{
	bolt;
	
	public float speed(){
		return 1.7f;
	}
	
	public float lifetime(){
		return 30f;
	}
	
	public int damage(){
		return 1;
	}
	
	public static KoruEntity createProjectile(long source, ProjectileType type, float rotation){
		KoruEntity entity = new KoruEntity(EntityType.projectile);
		entity.mapComponent(ProjectileComponent.class).type = type;
		entity.mapComponent(ProjectileComponent.class).setRotation(entity, rotation);
		entity.mapComponent(FadeComponent.class).lifetime = type.lifetime();
		entity.mapComponent(DamageComponent.class).source = source;
		entity.mapComponent(DamageComponent.class).damage = type.damage();
		return entity;
	}
}
