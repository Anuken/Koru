package io.anuke.koru.entities;

import io.anuke.koru.components.DamageComponent;
import io.anuke.koru.components.FadeComponent;
import io.anuke.koru.components.ProjectileComponent;
import io.anuke.koru.entities.types.Projectile;
import io.anuke.koru.renderers.EntityRenderer;
import io.anuke.ucore.core.Draw;

public enum ProjectileType{
	bolt, 
	slash{
		public float lifetime(){
			return 12f;
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
		
		public void draw(KoruEntity entity, EntityRenderer render, ProjectileComponent pro){
			render.draw(l->{
				l.layer = entity.getY()-4;
				Draw.alpha(1f-entity.get(FadeComponent.class).life/lifetime());
				Draw.rect(name(), entity.getX(), entity.getY(), rotation(entity)-45);
				Draw.color();
			});
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
	
	public void draw(KoruEntity entity, EntityRenderer render, ProjectileComponent pro){
		render.draw(l->{
			l.layer = entity.getY()-4;
			Draw.rect(name(), entity.getX(), entity.getY(), rotation(entity)-45);
		});
		
		render.drawShadow(name(), -1, entity);
	}
	
	public float rotation(KoruEntity entity){
		return entity.velocity().angle();
	}
	
	public int damage(){
		return 1;
	}
	
	public static KoruEntity createProjectile(long source, ProjectileType type, float rotation){
		return ProjectileType.createProjectile(source, type, rotation, -1);
	}
	
	public static KoruEntity createProjectile(long source, ProjectileType type, float rotation, int damage){
		KoruEntity entity = new KoruEntity(Projectile.class);
		entity.get(ProjectileComponent.class).type = type;
		//TODO
		entity.velocity().velocity.set(type.speed(), 0).rotate(rotation);
		
		entity.get(FadeComponent.class).lifetime = type.lifetime();
		
		entity.get(DamageComponent.class).source = source;
		entity.get(DamageComponent.class).damage = damage == -1 ? type.damage() : damage;
		return entity;
	}
}
