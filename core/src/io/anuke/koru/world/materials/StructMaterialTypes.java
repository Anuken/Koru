package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.graphics.KoruFacet;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.facet.FacetList;
import io.anuke.ucore.facet.Sorter;

public class StructMaterialTypes{
	
	public static class Torch extends Material{
		
		public Torch(String name){
			super(name, MaterialLayer.wall);
			color = new Color(0x744a28ff);
		}
		
		public void draw(Tile tile, FacetList group){
			
			//light overlay
			new KoruFacet("torchflame1"){
				public void draw(){
					sprite.setRegion(Draw.region("torchflame" + frame(tile)));
					super.draw();
				}
			}.light().tile(tile).centerX().add(group);
			
			//radius light
			new KoruFacet("light"){
				public void draw(){
					sprite.setOriginCenter();
					sprite.setScale(1f + (float)Math.sin(Timers.time()/7f+ tile.rand(100)/30f)/25f + (float)Math.random()/20f);
					
					super.draw();
				}
			}.light().set(tile.worldx(), tile.worldy()+6).center().color(0.5f, 0.4f, 0.2f).add(group);
			
			//actual torch
			new KoruFacet(name){
				public void draw(){
					Draw.sprite(sprite);
					Draw.color(1,1,1,sprite.getColor().a);
					Draw.crect("torchflame" + frame(tile), sprite.getX(), sprite.getY());
				}
			}.set(tile.worldx(), tile.worldy() + offset()).layer(tile.worldy()).centerX()
			.sort(Sorter.object)
			.addShadow(group, -offset()).add(group);
		}
		
		int frame(Tile tile){
			return (int)(1+(tile.rand(50)+Timers.time()/4)%4);
		}
	};
}
