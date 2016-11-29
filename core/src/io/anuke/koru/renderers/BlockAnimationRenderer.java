package io.anuke.koru.renderers;

import com.badlogic.gdx.Gdx;

import io.anuke.koru.Koru;
import io.anuke.koru.components.DataComponent;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.World;
import io.anuke.ucore.spritesystem.Renderable;
import io.anuke.ucore.spritesystem.RenderableList;

public class BlockAnimationRenderer extends EntityRenderer{
	public RenderableList list = new RenderableList();
	float lifetime = 50;
	float life;

	@Override
	protected void render(){
		life += Gdx.graphics.getDeltaTime()*60;
		
		for(Renderable r : list.renderables)
		r.sprite().setAlpha(1f-life/lifetime);
		
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
	}
}
