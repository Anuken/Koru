package io.anuke.koru.entities.types;

import io.anuke.koru.components.*;
import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.InputHandler;
import io.anuke.koru.renderers.PlayerRenderer;
import io.anuke.koru.systems.SyncSystem.SyncType;

public class Player extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new ConnectionComponent(),
				new RenderComponent(new PlayerRenderer()), new ColliderComponent(), 
				new WeaponComponent(),
				new SyncComponent(SyncType.player), new InputComponent(), 
				new HealthComponent(), new InventoryComponent(4,6));
	}
	
	public void init(KoruEntity entity){
		entity.get(InputComponent.class).input = new InputHandler(entity);
		entity.collider().collider.drag = 0.73f;
		entity.collider().collider.setSize(8, 6);
	}
	
	public boolean removeDeath(){
		return false;
	}
	
	public boolean unload(){
		return false;
	}

}
