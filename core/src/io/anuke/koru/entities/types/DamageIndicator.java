package io.anuke.koru.entities.types;

import io.anuke.koru.entities.ComponentList;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.renderers.IndicatorRenderer;
import io.anuke.koru.traits.*;

public class DamageIndicator extends EntityType{

	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new RenderComponent(new IndicatorRenderer()),
				new ChildComponent(), new TextTrait(), new FadeComponent(20));
	}

}
