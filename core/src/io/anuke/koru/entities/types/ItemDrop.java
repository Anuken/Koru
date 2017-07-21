package io.anuke.koru.entities.types;

import io.anuke.koru.components.*;
import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.network.SyncType;
import io.anuke.koru.renderers.ItemRenderer;

public class ItemDrop extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), 
				new RenderComponent(new ItemRenderer()),
				new SyncTrait(SyncType.position),
				new ItemTrait(), new VelocityComponent());
	}

}
