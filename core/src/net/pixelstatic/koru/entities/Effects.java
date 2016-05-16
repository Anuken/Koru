package net.pixelstatic.koru.entities;

import net.pixelstatic.koru.components.*;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.World;

import com.badlogic.gdx.graphics.Color;

public class Effects{
	
	public static void indicator(String text, float x, float y, float lifetime){
		indicator(text, Color.WHITE, x, y, lifetime);
	}
	
	public static void indicator(String text, Color color, float x, float y, float lifetime){
		KoruEntity entity = new KoruEntity(EntityType.damageindicator);
		entity.mapComponent(TextComponent.class).text = text;
		entity.mapComponent(TextComponent.class).color = color;
		entity.mapComponent(FadeComponent.class).lifetime = lifetime;
		entity.position().set(x, y);
		entity.sendSelf();
	}
	
	public static void indicator(String text, long parent, float lifetime){
		indicator(text, Color.WHITE, parent,lifetime);
	}
	
	public static void indicator(String text, Color color, long parent, float lifetime){
		KoruEntity entity = new KoruEntity(EntityType.damageindicator);
		entity.mapComponent(TextComponent.class).text = text;
		entity.mapComponent(TextComponent.class).color = color;
		entity.mapComponent(FadeComponent.class).lifetime = lifetime;
		entity.mapComponent(ChildComponent.class).parent = parent;
		entity.sendSelf();
	}
	
	public static void particle(String name, float x, float y, Color start, Color end, float velocity, float gravity){
		KoruEntity entity = new KoruEntity(EntityType.particle);
		entity.position().set(x, y);
		entity.mapComponent(ParticleComponent.class).set(name, start, end).setSpeed(gravity, velocity);
		entity.sendSelf();
	}
	
	public static void particle(String name, float x, float y, Color start, Color end){
		particle(name, x, y, start, end, 1f, 1f);
	}
	
	public static void particle(String name, float x, float y, Color color){
		particle(name, x, y, color, color, 1f, 1f);
	}
	
	public static void particle(String name, KoruEntity entity, Color color){
		particle(name, entity.getX(), entity.getY(), color, color, 1f, 1f);
	}
	
	public static void particle(KoruEntity entity, Color start, Color end, float velocity, float gravity){
		particle("spark", entity.getX(), entity.getY(), start, end, velocity, gravity);
	}
	
	public static void particle(KoruEntity entity, Color start, Color end){
		particle("spark", entity.getX(), entity.getY(), start, end, 1f, 1f);
	}
	
	public static void particle(KoruEntity entity, Color color){
		particle(entity, color, color);
	}
	
	public static void blockParticle(float x, float y, Material material){
		particle("spark", x, y, material.getColor(), material.getColor(), 1f, 1f);
	}
	
	public static void blockParticle(int x, int y, Material material){
		particle("spark", World.world(x), World.world(y), material.getColor(), material.getColor(), 1f, 1f);
	}
}
