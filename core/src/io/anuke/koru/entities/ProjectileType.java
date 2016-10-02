package io.anuke.koru.entities;

import io.anuke.koru.components.*;
import io.anuke.ucore.spritesystem.Renderable;

public enum ProjectileType{
	bolt;
	
	public float speed(){
		return 1.7f;
	}
	
	public float lifetime(){
		return 30f;
	}
	
	public void draw(KoruEntity entity, RenderComponent render){
		render.group.setPosition(entity.getX(), entity.getY());
		//render.layers.update(entity.getX(), entity.getY());
		float x = entity.getX(), y = entity.getY();
		for(Renderable renderable : render.group.list()){
			renderable.sprite().setPosition(x, y, true);
			//layer.rotation = entity.mapComponent(ProjectileComponent.class).getRotation() - 45;
		}
	}
	
	public void initSprite(KoruEntity entity, RenderComponent render){
		
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