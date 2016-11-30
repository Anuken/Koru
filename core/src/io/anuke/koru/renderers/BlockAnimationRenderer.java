package io.anuke.koru.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.koru.Koru;
import io.anuke.koru.components.DataComponent;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;
import io.anuke.koru.world.World;
import io.anuke.ucore.spritesystem.Renderable;
import io.anuke.ucore.spritesystem.RenderableList;
import io.anuke.ucore.spritesystem.SortProviders;
import io.anuke.ucore.spritesystem.SpriteRenderable;

public class BlockAnimationRenderer extends EntityRenderer{
	public RenderableList list = new RenderableList();
	float rspeed = 0.7f;
	float lifetime = 50;
	float life;

	@Override
	protected void render(){
		life += Gdx.graphics.getDeltaTime()*60;
		
		for(Renderable r : list.renderables)
		r.sprite().setAlpha(1f-life/lifetime);
		
		if(((Material)entity.mapComponent(DataComponent.class).data).getType() == MaterialType.tree)
		list.renderables.get(2).sprite().sprite.rotate(rspeed);
		
		if(life > lifetime){
			list.free();
			entity.removeSelf();
		}
	}

	@Override
	protected void initRender(){
		if(entity.mapComponent(DataComponent.class).data == null){
			throw new RuntimeException("No material specified in the data! Did you set the renderer material?");
		}
		Material material = (Material)entity.mapComponent(DataComponent.class).data;
		
		material.getType().draw(list, material, Koru.module(World.class).getTile((int)entity.getX(), (int)entity.getY()), (int)entity.getX(), (int)entity.getY());
		
		if(material.getType() == MaterialType.tree){
			
			SpriteRenderable bot = list.renderables.get(1).sprite();
			
			int theight = bot.sprite.getRegionHeight()/9;
			
			TextureRegion region = bot.sprite;
			
			TextureRegion tr = new TextureRegion(region);
			
			tr.setRegionHeight(region.getRegionHeight() - theight);
			
			region.setRegionY(region.getRegionY() + region.getRegionHeight()-theight);
			region.setRegionHeight(theight);
			
			bot.sprite.setSize(region.getRegionWidth(), theight);
			
			SpriteRenderable top = new SpriteRenderable(tr);
			top.setPosition(bot.sprite.getX(), bot.sprite.getY() + theight).setLayer(bot.layer()).setProvider(SortProviders.object);
			top.sprite.setOrigin(top.sprite.getWidth()/2, 0);
			top.add(list);
			
			if(Koru.module(ClientData.class).player.getX() < World.world((int)entity.getX())) rspeed *= -1f;
		}
	}
}
