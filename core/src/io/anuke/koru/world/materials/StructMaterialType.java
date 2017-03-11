package io.anuke.koru.world.materials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.spritesystem.*;

public class StructMaterialType{
	public static final BaseMaterialType torch = new BaseMaterialType(false, false){
		{
			color = new Color(0x744a28ff);
		}
		
		public void draw(RenderableList group, IMaterial material, Tile tile, int x, int y){
			
			//light overlay
			new SpriteRenderable(Resources.region("torchflame1")){
				public void draw(Batch batch){
					sprite.setRegion(Resources.region("torchflame" + frame(x,y)));
					super.draw(batch);
				}
			}.setPosition(tile(x), tile(y)).centerX().setLight().add(group);
			
			//radius light
			new SpriteRenderable(Resources.region("light")){
				public void draw(Batch batch){
					sprite.setOriginCenter();
					sprite.setScale(1f + (float)Math.sin(Gdx.graphics.getFrameId()/7f+rand(x,y,100)/30f)/25f + (float)Math.random()/20f);
					
					super.draw(batch);
				}
			}.setPosition(tile(x), tile(y)+6).center().setLight().setColor(0.5f, 0.4f, 0.2f).add(group);
			
			//actual torch
			new SpriteRenderable(Resources.region(material.name())){
				public void draw(Batch batch){
					sprite.draw(batch);
					batch.setColor(1,1,1,sprite.getColor().a);
					batch.draw(Resources.region("torchflame" + frame(x,y)), sprite.getX(), sprite.getY());
				}
			}.setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX()
			.addShadow(group, Resources.atlas(), -material.offset())
			.setProvider(Sorter.object).add(group);
		}
		
		int frame(int x, int y){
			return (int)(1+(rand(x,y,50)+Gdx.graphics.getFrameId()/4)%4);
		}
	};
	
	public static final BaseMaterialType chest = new BaseMaterialType(false, false){
		
		public void draw(RenderableList group, IMaterial material, Tile tile, int x, int y){
			
			RenderPool.sprite(Resources.region(material.name())).setPosition(tile(x), tile(y) + material.offset())
			.setLayer(tile(y)).centerX()
					.addShadow(group, Resources.atlas(), -material.offset()).setProvider(Sorter.object).add(group);
		}
	};
	
	public static final BaseMaterialType workbench = new BaseMaterialType(false, false){
		
		public void draw(RenderableList group, IMaterial material, Tile tile, int x, int y){

			RenderPool.sprite(Resources.region(material.name())).setPosition(tile(x), tile(y) + material.offset())
			.setLayer(tile(y)).centerX()
					.addShadow(group, Resources.atlas(), -material.offset()).setProvider(Sorter.object).add(group);
		}
	};
}
