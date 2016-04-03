package net.pixelstatic.koru.listeners;

import net.pixelstatic.koru.components.ChildComponent;
import net.pixelstatic.koru.components.HealthComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;

public class DamageListener extends CollisionListener{

	@Override
	boolean accept(KoruEntity entity, KoruEntity other){
		return entity.mapComponent(HealthComponent.class) != null;
	}

	@Override
	void collision(KoruEntity entity, KoruEntity other){
		KoruEntity damage = new KoruEntity(EntityType.damageindicator);
		damage.mapComponent(ChildComponent.class).parent = entity.getID();
		damage.sendSelf();
	}

}
