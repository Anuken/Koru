package io.anuke.koru.entities.types;

import io.anuke.koru.components.*;
import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.SyncType;
import io.anuke.koru.renderers.EnemyRenderer;

public class TestEntity extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(),
				new RenderComponent(new EnemyRenderer()), 
				new ColliderComponent(),
				new SyncTrait(SyncType.physics),
				new HealthComponent());
	}
	
	public void init(KoruEntity entity){
		entity.collider().setSize(7, 7);
	}

}
