package io.anuke.koru.renderers;

import io.anuke.koru.traits.ItemTrait;
import io.anuke.ucore.core.Draw;

public class ItemRenderer extends EntityRenderer{

	@Override
	protected void init(){
		String itemname = entity.get(ItemTrait.class).stack.item.name();
		String region = Draw.hasRegion(itemname + "chunk") ? itemname + "chunk" : itemname + "item";
		
		draw(p->{
			p.layer = entity.getY();
			
			Draw.grect(region, entity.getX(), entity.getY()-2);
		});
		
		drawShadow(region, entity);
	}
}
