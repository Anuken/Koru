package io.anuke.koru.entities.types;

import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.renderers.BlockAnimationRenderer;
import io.anuke.koru.traits.MaterialTrait;
import io.anuke.koru.traits.PositionComponent;
import io.anuke.koru.traits.RenderComponent;

public class BlockAnimation extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new RenderComponent(new BlockAnimationRenderer()), new MaterialTrait());
	}

}
