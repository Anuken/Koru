package io.anuke.koru.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.koru.Koru;
import io.anuke.koru.items.BlockRecipe;
import io.anuke.koru.network.packets.RecipeSelectPacket;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialLayer;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.scene.utils.ClickListener;

public class RecipeView extends Table{
	final int slotsize = 64;
	float pscale = slotsize/16;
	Slot selected;
	
	public RecipeView(){
		for(BlockRecipe recipe : BlockRecipe.getAll()){
			Slot slot = new Slot(recipe, recipe.id());
			add(slot).size(slotsize);
		}
		
		background("button-window-bg");
		pad(4);
	}
	
	class Slot extends Element{
		public final int index;
		public final ClickListener click;
		public final BlockRecipe recipe;
		
		public Slot(BlockRecipe recipe, int index) {
			this.index = index;
			this.recipe = recipe;
			
			click = clicked(()->{
				selected = Slot.this;
					
				Koru.control.player.get(InventoryTrait.class).recipe = index;
					
				RecipeSelectPacket packet = new RecipeSelectPacket();
				packet.recipe = index;
				Koru.network.client.sendTCP(packet);
			});

		}

		public void draw(Batch batch, float alpha){
			Draw.color(getColor());
			patch(Koru.control.player.get(InventoryTrait.class).recipe == index ? "slotset" : (click.isOver() ? "slotselect2" : "slot2"));
			
			Material result = recipe.result();
			TextureRegion region = Draw.region(result.name());
			
			float w = region.getRegionWidth()*pscale,h = region.getRegionHeight()*pscale;
			
			if(result.layer() == MaterialLayer.floor){
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}else{
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, 4*pscale+ getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}
			
			Draw.reset();
		}
	}
}
