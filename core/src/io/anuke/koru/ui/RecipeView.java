package io.anuke.koru.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.koru.Koru;
import io.anuke.koru.items.Recipe;
import io.anuke.koru.network.Net;
import io.anuke.koru.network.packets.RecipeSelectPacket;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.scene.utils.ClickListener;

public class RecipeView extends Table{
	final int slotsize = 64;
	float pscale = slotsize/16;
	Slot selected;
	
	public RecipeView(){
		for(Recipe recipe : Recipe.getAll()){
			Slot slot = new Slot(recipe, recipe.id());
			add(slot).size(slotsize);
		}
		
		background("button-window-bg");
		pad(4);
	}
	
	class Slot extends Element{
		public final int index;
		public final ClickListener click;
		public final Recipe recipe;
		
		public Slot(Recipe recipe, int index) {
			this.index = index;
			this.recipe = recipe;
			
			click = clicked(()->{
				selected = Slot.this;
					
				Koru.control.player.get(InventoryTrait.class).recipe = index;
					
				RecipeSelectPacket packet = new RecipeSelectPacket();
				packet.recipe = index;
				Net.send(packet);
			});

		}

		public void draw(Batch batch, float alpha){
			
			
			patch(Koru.control.player.get(InventoryTrait.class).recipe == index ? "slotset" : (click.isOver() ? "slotselect2" : "slot2"));
			
			TextureRegion region = Draw.region(recipe.result().name() + "item");
			
			float scale = 4f;
			
			Draw.color(0f, 0f, 0f, 0.1f * alpha);
			
			Draw.rect(recipe.result().name() + "item", x + width/2f, y + height/2f - scale, 
					region.getRegionWidth()*scale, region.getRegionHeight()*scale);
			
			Draw.color(getColor());
			
			Draw.rect(recipe.result().name() + "item", x + width/2f, y + height/2f, 
					region.getRegionWidth()*scale, region.getRegionHeight()*scale);
			
			
			/*
			Material result = recipe.result();
			TextureRegion region = Draw.region(result.name());
			
			float w = region.getRegionWidth()*pscale,h = region.getRegionHeight()*pscale;
			
			if(result.layer() == MaterialLayer.floor){
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}else{
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, 4*pscale+ getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}
			
			Draw.reset();
			*/
		}
	}
}
