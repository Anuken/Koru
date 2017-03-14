package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import io.anuke.koru.graphics.KoruRenderable;
import io.anuke.koru.graphics.RenderPool;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.spritesystem.RenderableList;
import io.anuke.ucore.spritesystem.Sorter;
import io.anuke.ucore.util.Timers;

public class StructMaterialType{
	public static final BaseMaterialType torch = new BaseMaterialType(false, false){
		{
			color = new Color(0x744a28ff);
		}
		
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			
			//light overlay
			new KoruRenderable("torchflame1"){
				public void draw(Batch batch){
					sprite.setRegion(Resources.region("torchflame" + frame(x,y)));
					super.draw(batch);
				}
			}.light().set(tile(x), tile(y)).centerX().add(group);
			
			//radius light
			new KoruRenderable("light"){
				public void draw(Batch batch){
					sprite.setOriginCenter();
					sprite.setScale(1f + (float)Math.sin(Timers.time()/7f+rand(x,y,100)/30f)/25f + (float)Math.random()/20f);
					
					super.draw(batch);
				}
			}.light().set(tile(x), tile(y)+6).center().color(0.5f, 0.4f, 0.2f).add(group);
			
			//actual torch
			new KoruRenderable(material.name()){
				public void draw(Batch batch){
					sprite.draw(batch);
					batch.setColor(1,1,1,sprite.getColor().a);
					batch.draw(Resources.region("torchflame" + frame(x,y)), sprite.getX(), sprite.getY());
				}
			}.set(tile(x), tile(y) + material.offset()).layer(tile(y)).centerX()
			.sort(Sorter.object)
			.addShadow(group, -material.offset()).add(group);
		}
		
		int frame(int x, int y){
			return (int)(1+(rand(x,y,50)+Timers.time()/4)%4);
		}
	};
	
	public static final BaseMaterialType chest = new BaseMaterialType(false, false){
		
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			
			RenderPool.get(material.name())
			.set(tile(x), tile(y) + material.offset())
			.layer(tile(y)).centerX()
			.addShadow(group, -material.offset())
					.sort(Sorter.object).add(group);
		}
	};
	
	public static final BaseMaterialType workbench = new BaseMaterialType(false, false){
		
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){

			RenderPool.get(material.name())
			.set(tile(x), tile(y) + material.offset())
			.layer(tile(y)).centerX()
			.addShadow(group, -material.offset())
					.sort(Sorter.object).add(group);
		}
	};
}
