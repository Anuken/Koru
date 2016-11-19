package io.anuke.koru.renderers;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.spritesystem.SortProviders;
import io.anuke.ucore.spritesystem.SpriteRenderable;
import io.anuke.ucore.spritesystem.TextRenderable;

import com.badlogic.gdx.graphics.Color;


public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		render.group.get("player").sprite().setPosition(entity.getX(), entity.getY()).centerX();
		render.group.get("shadow").sprite().setPosition(entity.getX(), entity.getY()).center();
		
		render.group.get("name").setPosition(entity.getX(), entity.getY());
		//render.layers.update(entity.getX(), entity.getY());
		//render.layers.layer("player").setPosition(entity.getX(), entity.getY()).yLayer(false).addBlobShadow(-0.5f).add();
		//render.layers.layer("player").addReflection();
	}
	
	@Override
	public void initRender(){
		new SpriteRenderable(Resources.region("player"))
		.addShadow(render.group, Resources.atlas())
		.setProvider(SortProviders.object)
		.add("player", render.group);
		
		new TextRenderable(Resources.font(), entity.getComponent(ConnectionComponent.class).local ? "" : entity.getComponent(ConnectionComponent.class).name)
		.center()
		.setColor(Color.CORAL)
		.setProvider(SortProviders.object)
		.add("name", render.group);
	}

}
