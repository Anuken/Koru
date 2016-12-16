package io.anuke.koru.renderers;

import io.anuke.koru.components.ItemComponent;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.spritesystem.SortProviders;
import io.anuke.ucore.spritesystem.SpriteRenderable;

public class ItemRenderer extends EntityRenderer{

	@Override
	protected void render(){
		render.group.get("item").sprite().setPosition(entity.getX(), entity.getY()).setLayer(entity.getY()+2).center();
		render.group.get("shadow").sprite().setPosition(entity.getX(), entity.getY()-2).center();
	}

	@Override
	protected void initRender(){
		String itemname = entity.get(ItemComponent.class).stack.item.name();
		
		new SpriteRenderable(Resources.region(Resources.hasRegion(itemname + "chunk") ? itemname + "chunk" : itemname + "item"))
		.addShadow(render.group, Resources.atlas())
		.setProvider(SortProviders.object)
		.add("item", render.group);
	}
}
