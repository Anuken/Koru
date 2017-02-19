package io.anuke.koru.renderers;

import io.anuke.koru.components.ItemComponent;
import io.anuke.koru.graphics.Draw;
import io.anuke.koru.utils.Resources;

public class ItemRenderer extends EntityRenderer{

	@Override
	protected void init(){
		String itemname = entity.get(ItemComponent.class).stack.item.name();
		String region = Resources.hasRegion(itemname + "chunk") ? itemname + "chunk" : itemname + "item";
		
		draw(p->{
			p.layer = entity.getY();
			
			Draw.grect(region, entity.getX(), entity.getY()-2);
		});
		
		drawShadow(region, entity);
	}
}
