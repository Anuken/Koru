package net.pixelstatic.koru.entities;

import net.pixelstatic.koru.components.FadeComponent;
import net.pixelstatic.koru.components.ProjectileComponent;
import net.pixelstatic.koru.components.RenderComponent;
import net.pixelstatic.koru.sprites.Layer;

public enum ProjectileType{
	bolt;
	
	public float speed(){
		return 1.7f;
	}
	
	public float lifetime(){
		return 30f;
	}
	
	public void draw(KoruEntity entity, RenderComponent render){
		//render.layers.update(entity.getX(), entity.getY());
		float x = entity.getX(), y = entity.getY();
		for(Layer layer : render.layers.values()){
			layer.update(x, y);
			layer.rotation = entity.mapComponent(ProjectileComponent.class).getRotation() - 45;
		}
	}
	
	public void initSprite(KoruEntity entity, RenderComponent render){
		
	}
	
	public static KoruEntity createProjectile(ProjectileType type, float rotation){
		KoruEntity entity = new KoruEntity(EntityType.projectile);
		entity.mapComponent(ProjectileComponent.class).type = type;
		entity.mapComponent(ProjectileComponent.class).setRotation(entity, rotation);
		entity.mapComponent(FadeComponent.class).lifetime = type.lifetime();
		return entity;
	}
}
