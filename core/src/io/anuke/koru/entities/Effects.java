package io.anuke.koru.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.koru.components.*;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.modules.World;
import io.anuke.koru.world.Material;

public class Effects{

	public static void drops(float x, float y, ItemStack[] stacks){
		int stepsize = 5;
		float range = 5;
		
		for(ItemStack stack : stacks){
			
			int amount = stack.amount;
			while(amount >= 0){
				KoruEntity item = new KoruEntity(EntityType.item);
				item.get(ItemComponent.class).stack = new ItemStack(stack.item, amount > stepsize ? stepsize : amount);
				item.position().set(x + MathUtils.random(-range,range), y + MathUtils.random(-range,range));
				item.add().send();
				amount -= stepsize;
			}
		}
	}

	public static void indicator(String text, float x, float y, float lifetime){
		indicator(text, Color.WHITE, x, y, lifetime);
	}

	public static void indicator(String text, Color color, float x, float y, float lifetime){
		KoruEntity entity = new KoruEntity(EntityType.damageindicator);
		entity.get(TextComponent.class).text = text;
		entity.get(TextComponent.class).color = color;
		entity.get(FadeComponent.class).lifetime = lifetime;
		entity.position().set(x, y);
		entity.send();
	}

	public static void indicator(String text, long parent, float lifetime){
		indicator(text, Color.WHITE, parent, lifetime);
	}

	public static void indicator(String text, Color color, long parent, float lifetime){
		KoruEntity entity = new KoruEntity(EntityType.damageindicator);
		entity.get(TextComponent.class).text = text;
		entity.get(TextComponent.class).color = color;
		entity.get(FadeComponent.class).lifetime = lifetime;
		entity.get(ChildComponent.class).parent = parent;
		entity.send();
	}

	public static void particle(String name, float x, float y, Color start, Color end, float velocity, float gravity){
		KoruEntity entity = new KoruEntity(EntityType.particle);
		entity.position().set(x, y);
		entity.get(ParticleComponent.class).set(name, start, end).setSpeed(gravity, velocity);
		entity.send();
	}

	public static void particle(Material material, float x, float y){
		KoruEntity entity = new KoruEntity(EntityType.particle);
		entity.position().set(x, y);
		entity.get(ParticleComponent.class).set(material);
		entity.send();
	}

	public static void block(Material material, int x, int y){
		KoruEntity entity = new KoruEntity(EntityType.blockanimation);
		entity.position().set(x * World.tilesize, y * World.tilesize);
		entity.get(DataComponent.class).data = material;
		entity.send();
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

	public static void blockBreakParticle(float x, float y, Material material){
		particle("break", x, y, material.getColor(), material.getColor(), 1f, 1f);
	}

	public static void blockParticle(int x, int y, Material material){
		particle("spark", World.world(x), World.world(y), material.getColor(), material.getColor(), 1f, 1f);
	}
}
