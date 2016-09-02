package net.pixelstatic.koru.renderers;

import net.pixelstatic.koru.utils.Resources;
import net.pixelstatic.utils.spritesystem.SortProviders;
import net.pixelstatic.utils.spritesystem.SpriteRenderable;


public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		render.group.get("player").sprite().setPosition(entity.getX(), entity.getY()).centerX();
		render.group.get("shadow").sprite().setPosition(entity.getX(), entity.getY()).center();
		//render.layers.update(entity.getX(), entity.getY());
		//render.layers.layer("player").setPosition(entity.getX(), entity.getY()).yLayer(false).addBlobShadow(-0.5f).add();
		//render.layers.layer("player").addReflection();
	}
	
	@Override
	public void initRender(){
		new SpriteRenderable(Resources.findRegion("player"))
		.addShadow(render.group, Resources.getAtlas())
		.setProvider(SortProviders.object)
		.add("player", render.group);
	}

}
