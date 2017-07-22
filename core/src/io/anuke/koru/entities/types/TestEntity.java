package io.anuke.koru.entities.types;

import io.anuke.koru.network.SyncType;
import io.anuke.koru.traits.SyncTrait;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.traits.ColliderTrait;
import io.anuke.ucore.ecs.extend.traits.HealthTrait;
import io.anuke.ucore.ecs.extend.traits.PosTrait;

public class TestEntity extends Prototype{

	@Override
	public TraitList traits(){
		return new TraitList(
			new PosTrait(),
			//TODO
			//new RenderComponent(new EnemyRenderer()), 
			new ColliderTrait(7, 7),
			new SyncTrait(SyncType.physics),
			new HealthTrait()
		);
	}

}
