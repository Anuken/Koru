package io.anuke.koru.entities.types;

import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.TraitList;

public class Player extends Prototype{
/*
	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new ConnectionComponent(),
				new RenderComponent(new PlayerRenderer()), new ColliderComponent(), 
				new WeaponComponent(), new LoadChunksComponent(),
				new SyncComponent(SyncType.player), new InputComponent(), 
				new HealthComponent(), new InventoryComponent(4,6));
	}
	
	public void init(KoruEntity entity){
		entity.get(InputComponent.class).input = new InputHandler(entity);
		entity.collider().setSize(8, 6);
	}
	*/
	
	//TODO
	public boolean removeDeath(){
		return false;
	}
	
	//TODO
	public boolean unload(){
		return false;
	}

	@Override
	public TraitList traits(){
		return new TraitList(
			//TODO
		);
	}

}
