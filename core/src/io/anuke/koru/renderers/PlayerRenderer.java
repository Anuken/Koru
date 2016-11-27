package io.anuke.koru.renderers;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.spritesystem.SortProviders;
import io.anuke.ucore.spritesystem.SpriteRenderable;
import io.anuke.ucore.spritesystem.TextRenderable;

public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		
		render.group.get("crab").sprite().setPosition(entity.getX(), entity.getY()).centerX();
		
		render.group.get("crab").sprite().sprite.setRegion(Resources.region("crab" + dir() + 
				(render.walkframe > 0.001f ? ("-"+ ((int)(render.walkframe/7)%3)) : "")));
		render.group.get("crab").sprite().sprite.setFlip(flip(), false);
		
		render.group.get("item").sprite()
		.setRegion(Resources.region(entity.getComponent(InventoryComponent.class).hotbarStack() == null ? 
				"clear" : entity.getComponent(InventoryComponent.class).hotbarStack().item.name()));
		
		render.group.get("item").sprite().setLayer(entity.getY() + 0.0001f)
		.setPosition(entity.getX() + shift(1), entity.getY()+5).centerX().sprite().sprite.setFlip(flip2(), false);
		
		render.group.get("shadow").sprite().setPosition(entity.getX(), entity.getY()+1).center();
		render.group.get("name").setPosition(entity.getX(), entity.getY());
	}
	
	@Override
	public void initRender(){
		
		new SpriteRenderable(Resources.region("crab"))
		.addShadow(render.group, Resources.atlas())
		.setProvider(SortProviders.object)
		.add("crab", render.group);
		
		new SpriteRenderable(Resources.region("woodaxeitem"))
		.setProvider(SortProviders.object)
		.add("item", render.group);
		
		new TextRenderable(Resources.font(), entity.getComponent(ConnectionComponent.class).local ? "" : entity.getComponent(ConnectionComponent.class).name)
		.center()
		.setColor(Color.CORAL)
		.setProvider(SortProviders.object)
		.add("name", render.group);
		
	}
}
