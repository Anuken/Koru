package io.anuke.koru.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.Recipe;
import io.anuke.koru.items.Recipes;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.packets.RecipeSelectPacket;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;
import io.anuke.ucore.graphics.Hue;

public class RecipeMenu extends VisTable{
	final int slotsize = 64;
	float pscale = slotsize/16;
	Requirement req = new Requirement();
	Slot selected;
	
	public RecipeMenu(){
		for(int i = 0; i < Recipes.values().length; i ++){
			Slot slot = new Slot(Recipes.values()[i],i,0);
			add(slot).size(slotsize);
		}
		row();
		
		add(req).colspan(Recipes.values().length).height(64);
	}
	
	public void act(float delta){
		setVisible(Gdx.input.isKeyPressed(Keys.F));
	}
	
	public void draw(Batch batch, float alpha){
		batch.setColor(Hue.lightness(95/255f));
		batch.draw(Resources.region("blank"), getX()-pscale, getY()-pscale, getWidth()+pscale*2, getHeight()+pscale*2);
		batch.setColor(Color.WHITE);
		
		super.draw(batch, alpha);
	}
	
	class Requirement extends Actor{
		ItemStack[] stacks;
		
		public void draw(Batch batch, float alpha){
			if(stacks == null) return;
			for(int i = 0; i < stacks.length; i ++){
				TextureRegion region = Resources.region(stacks[i].item.name() + "item");
				float w = region.getRegionWidth()*pscale, h = region.getRegionHeight()*pscale;
				float x = getX() + getWidth()/2 - region.getRegionWidth()/2*pscale - (stacks.length-1)*8*pscale + i*16*pscale, 
						y = getY()+2*pscale;
				
				batch.draw(region, x, y, w, h);
				
				Resources.font().draw(batch, stacks[i].amount + "x", x, y + 20, 0, Align.bottomLeft, false);
			}
		}
		
		public void set(ItemStack[] stack){
			this.stacks = stack;
		}
	}
	
	class Slot extends Actor{
		public final int x, y;
		public final ClickListener click;
		public final Recipe recipe;
		
		public Slot(Recipe recipe, int x, int y) {
			this.x = x;
			this.y = y;
			this.recipe = recipe;

			addListener((click = new ClickListener(){
				public void clicked (InputEvent event, float x2, float y2) {
					selected = Slot.this;
					req.set(recipe.requirements());
					
					Koru.module(ClientData.class).player.getComponent(InventoryComponent.class).recipe = x;
					
					RecipeSelectPacket packet = new RecipeSelectPacket();
					packet.recipe = x;
					Koru.module(Network.class).client.sendTCP(packet);
				}
			}));
		}

		public String toString(){
			return "Slot " + x + ", " + y;
		}

		public void draw(Batch batch, float alpha){
			batch.setColor(getColor());
			batch.draw(Resources.region(selected == this ? "slotset" : (click.isOver() ? "slotselect2" : "slot2")), getX(), getY(), getWidth(), getHeight());
			
			Material result = recipe.result();
			TextureRegion region = Resources.region(result.name());
			
			float w = region.getRegionWidth()*pscale,h = region.getRegionHeight()*pscale;
			
			if(result.getType() == MaterialType.tile){
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}else{
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, 4*pscale+ getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}
		}
	}
}
