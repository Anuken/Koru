package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.graphics.KoruRenderable;
import io.anuke.koru.graphics.RenderPool;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.facet.FacetList;
import io.anuke.ucore.facet.Sorter;

public class StructMaterialTypes{
	
	public static final MaterialType
	
	torch = new MaterialType(false, false){
		{
			color = new Color(0x744a28ff);
		}
		
		public void draw(FacetList group, Material material, Tile tile, int x, int y){
			
			//light overlay
			new KoruRenderable("torchflame1"){
				public void draw(){
					sprite.setRegion(Draw.region("torchflame" + frame(x,y)));
					super.draw();
				}
			}.light().set(tile(x), tile(y)).centerX().add(group);
			
			//radius light
			new KoruRenderable("light"){
				public void draw(){
					sprite.setOriginCenter();
					sprite.setScale(1f + (float)Math.sin(Timers.time()/7f+rand(x,y,100)/30f)/25f + (float)Math.random()/20f);
					
					super.draw();
				}
			}.light().set(tile(x), tile(y)+6).center().color(0.5f, 0.4f, 0.2f).add(group);
			
			//actual torch
			new KoruRenderable(material.name()){
				public void draw(){
					Draw.sprite(sprite);
					Draw.color(1,1,1,sprite.getColor().a);
					Draw.crect("torchflame" + frame(x,y), sprite.getX(), sprite.getY());
				}
			}.set(tile(x), tile(y) + material.offset()).layer(tile(y)).centerX()
			.sort(Sorter.object)
			.addShadow(group, -material.offset()).add(group);
		}
		
		int frame(int x, int y){
			return (int)(1+(rand(x,y,50)+Timers.time()/4)%4);
		}
	},
	
	chest = new MaterialType(false, false){
		
		public void draw(FacetList group, Material material, Tile tile, int x, int y){
			
			RenderPool.get(material.name())
			.set(tile(x), tile(y) + material.offset())
			.layer(tile(y)).centerX()
			.addShadow(group, -material.offset())
					.sort(Sorter.object).add(group);
		}
	},
	
	workbench = new MaterialType(false, false){
		
		public void draw(FacetList group, Material material, Tile tile, int x, int y){

			RenderPool.get(material.name())
			.set(tile(x), tile(y) + material.offset())
			.layer(tile(y)).centerX()
			.addShadow(group, -material.offset())
					.sort(Sorter.object).add(group);
		}
	};
}
