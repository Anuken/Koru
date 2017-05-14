package io.anuke.koru.listeners;

import io.anuke.koru.components.*;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.types.DamageIndicator;

public class DamageListener extends CollisionListener{

	@Override
	boolean accept(KoruEntity entity, KoruEntity other){
		return entity.get(HealthComponent.class) != null && other.get(DamageComponent.class) != null;
	}

	@Override
	void contact(KoruEntity entity, KoruEntity other){
		HealthComponent health = entity.get(HealthComponent.class);
		int amount = other.get(DamageComponent.class).damage;
		health.health -= amount;
		
		KoruEntity damage = new KoruEntity(DamageIndicator.class);
		damage.get(ChildComponent.class).parent = entity.getID();
		damage.getComponent(TextComponent.class).text = amount + "";
		damage.position().set(other.getX(), other.getY() + other.get(ColliderComponent.class).height);
		
		if(health.health <= 0){
			entity.getType().onDeath(entity, other);
		}else{
			damage.send();
		}
	}

}
