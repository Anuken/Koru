package io.anuke.koru.renderers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.koru.Koru;
import io.anuke.koru.components.MaterialComponent;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.modules.World;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialTypes;
import io.anuke.ucore.spritesystem.*;

public class BlockAnimationRenderer extends EntityRenderer{
	public RenderableList list = new RenderableList();
	float rspeed = 0.7f;
	float lifetime = 50;
	float life;

	@Override
	protected void render(){
		life += Koru.delta();
		
		for(Renderable r : list.renderables)
		r.sprite().alpha(1f-life/lifetime);
		
		if(((Material)entity.get(MaterialComponent.class).material()).getType() == MaterialTypes.tree)
		list.renderables.get(2).sprite().sprite.rotate(rspeed*Koru.delta());
		
		if(life > lifetime){
			list.free();
			entity.remove();
		}
	}

	@Override
	protected void init(){
		if(entity.get(MaterialComponent.class).matid == 0){
			throw new RuntimeException("No material specified in the data! Did you set the renderer material?");
		}
		
		Material material = (Material)entity.get(MaterialComponent.class).material();
		
		int x = (int)(entity.getX()/World.tilesize);
		int y = (int)(entity.getY()/World.tilesize);
		
		material.getType().draw(list, material, Koru.module(World.class).getWorldTile(x, y), x, y);
		
		if(material.getType() == MaterialTypes.tree){
			
			SpriteRenderable bot = list.renderables.peek().sprite();
			
			int theight = bot.sprite.getRegionHeight()/9;
			
			TextureRegion region = bot.sprite;
			
			TextureRegion tr = new TextureRegion(region);
			
			tr.setRegionHeight(region.getRegionHeight() - theight);
			
			region.setRegionY(region.getRegionY() + region.getRegionHeight()-theight);
			region.setRegionHeight(theight);
			
			bot.sprite.setSize(region.getRegionWidth(), theight);
			
			SpriteRenderable top = new SpriteRenderable(tr);
			top.set(bot.sprite.getX(), bot.sprite.getY() + theight).layer(bot.getLayer()).sort(Sorter.object);
			top.sprite.setOrigin(top.sprite.getWidth()/2, 0);
			top.add(list);
			
			if(Koru.module(ClientData.class).player.getX() < entity.getX()) rspeed *= -1f;
		}
	}
}
