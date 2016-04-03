package net.pixelstatic.koru.listeners;

import net.pixelstatic.koru.components.*;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;

public class DamageListener extends CollisionListener{

	@Override
	boolean accept(KoruEntity entity, KoruEntity other){
		return entity.mapComponent(HealthComponent.class) != null && other.mapComponent(DamageComponent.class) != null;
	}

	@Override
	void collision(KoruEntity entity, KoruEntity other){
		HealthComponent health = entity.mapComponent(HealthComponent.class);
		int amount = other.mapComponent(DamageComponent.class).damage;
		health.health -= amount;
		
		KoruEntity damage = new KoruEntity(EntityType.damageindicator);
		damage.mapComponent(ChildComponent.class).parent = entity.getID();
		damage.getComponent(TextComponent.class).text = amount + "";
		damage.sendSelf();
		
		if(health.health <= 0){
			entity.getType().deathEvent(entity, other);
		}
	}

}
