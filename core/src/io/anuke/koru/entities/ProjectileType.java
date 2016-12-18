package io.anuke.koru.entities;

import io.anuke.koru.components.*;
import io.anuke.ucore.spritesystem.Renderable;

public enum ProjectileType{
	bolt, 
	slash{
		public float lifetime(){
			return 8f;
		}
		
		public float speed(){
			return 2.4f;
		}
		
		public float hitsize(){
			return 18f;
		}
		
		public boolean pierce(){
			return true;
		}
		
		public void draw(KoruEntity entity, RenderComponent render){
			super.draw(entity, render);
			render.group.get("bolt").sprite().setAlpha(entity.mapComponent(FadeComponent.class).scaled());
		}
	};
	
	public boolean pierce(){
		return false;
	}
	
	public float hitsize(){
		return 3f;
	}
	
	public float speed(){
		return 3f;
	}
	
	public float lifetime(){
		return 30f;
	}
	
	public void draw(KoruEntity entity, RenderComponent render){
		render.group.setPosition(entity.getX(), entity.getY());
		float x = entity.getX(), y = entity.getY();
		for(Renderable renderable : render.group.list()){
			renderable.sprite().setPosition(x, y, true);
			renderable.sprite().sprite.setRotation(entity.mapComponent(ProjectileComponent.class).getRotation() - 45);
		}
	}
	
	public void initSprite(KoruEntity entity, RenderComponent render){
		
	}
	
	public int damage(){
		return 1;
	}
	
	public static KoruEntity createProjectile(long source, ProjectileType type, float rotation){
		return ProjectileType.createProjectile(source, type, rotation, -1);
	}
	
	public static KoruEntity createProjectile(long source, ProjectileType type, float rotation, int damage){
		KoruEntity entity = new KoruEntity(EntityType.projectile);
		entity.mapComponent(ProjectileComponent.class).type = type;
		entity.mapComponent(ProjectileComponent.class).setRotation(entity, rotation);
		entity.mapComponent(VelocityComponent.class).velocity.set(type.speed(), 0).rotate(rotation);
		entity.mapComponent(FadeComponent.class).lifetime = type.lifetime();
		entity.mapComponent(HitboxComponent.class).entityRect().setSize(type.hitsize());
		entity.mapComponent(DamageComponent.class).source = source;
		entity.mapComponent(DamageComponent.class).damage = damage == -1 ? type.damage() : damage;
		return entity;
	}
}
