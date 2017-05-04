package io.anuke.koru.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.items.BlockRecipe;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.packets.RecipeSelectPacket;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialTypes;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.scene.utils.ClickListener;

public class RecipeMenu extends Table{
	final int slotsize = 64;
	float pscale = slotsize/16;
	Requirement req = new Requirement();
	Slot selected;
	
	public RecipeMenu(){
		for(BlockRecipe recipe : BlockRecipe.getAll()){
			Slot slot = new Slot(recipe, recipe.id(), 0);
			add(slot).size(slotsize);
		}
		row();
		
		add(req).colspan(BlockRecipe.getAll().size()).height(64);
	}
	
	public void act(float delta){
		setVisible(Gdx.input.isKeyPressed(Keys.F));
	}
	
	public void draw(Batch batch, float alpha){
		batch.setColor(Hue.lightness(95/255f));
		batch.draw(Resources.region("blank"), getX()-pscale, getY()-pscale + getHeight()/2, getWidth()+pscale*2, getHeight()/2+pscale*2);
		batch.draw(Resources.region("blank"), getX() + getWidth()/2-slotsize/2 - pscale, getY() - pscale, slotsize + pscale*2, slotsize + pscale*2);
		
		batch.setColor(Color.WHITE);
		batch.draw(Resources.region("slot2"), getX() + getWidth()/2-slotsize/2, getY(), slotsize, slotsize);
		
		super.draw(batch, alpha);
	}
	
	class Requirement extends Element{
		ItemStack[] stacks;
		
		public void draw(Batch batch, float alpha){
			if(stacks == null) return;
			InventoryComponent inv = Koru.module(ClientData.class).player.get(InventoryComponent.class);
			for(int i = 0; i < stacks.length; i ++){
				TextureRegion region = Resources.region(stacks[i].item.name() + "item");
				float w = region.getRegionWidth()*pscale, h = region.getRegionHeight()*pscale;
				float x = getX() + getWidth()/2 - region.getRegionWidth()/2*pscale - (stacks.length-1)*8*pscale + i*16*pscale, 
						y = getY()+2*pscale;
				
				batch.draw(region, x, y, w, h);
				
				int amount = Math.min(inv.getAmountOf(stacks[i].item), stacks[i].amount);
				Resources.font2().setColor(amount < stacks[i].amount ? new Color(1f,0.4f,0.4f,1f) : Color.WHITE);
				
				Resources.font2().draw(batch, amount + "/" + stacks[i].amount, x-2, y + 20, 0, Align.bottomLeft, false);
				
				Resources.font2().setColor(Color.WHITE);
			}
		}
		
		public void set(ItemStack[] stack){
			this.stacks = stack;
		}
	}
	
	class Slot extends Element{
		public final int x, y;
		public final ClickListener click;
		public final BlockRecipe recipe;
		
		public Slot(BlockRecipe recipe, int x, int y) {
			this.x = x;
			this.y = y;
			this.recipe = recipe;
			
			click = clicked(()->{
				selected = Slot.this;
				req.set(recipe.requirements());
					
				Koru.module(ClientData.class).player.getComponent(InventoryComponent.class).recipe = x;
					
				RecipeSelectPacket packet = new RecipeSelectPacket();
				packet.recipe = x;
				Koru.module(Network.class).client.sendTCP(packet);
			});

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
			
			if(result.getType() == MaterialTypes.tile){
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}else{
				batch.draw(region, getX() + getWidth()/2-region.getRegionWidth()*pscale/2, 4*pscale+ getY() + getHeight()/2-region.getRegionHeight()*pscale/2, w,h);
			}
		}
	}
}
