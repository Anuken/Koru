package io.anuke.koru.entities.types;

import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.components.MaterialTrait;
import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.renderers.BlockAnimationRenderer;

public class BlockAnimation extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new RenderComponent(new BlockAnimationRenderer()), new MaterialTrait());
	}

}
