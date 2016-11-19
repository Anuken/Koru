package io.anuke.koru.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.utils.Resources;

public class InventoryWidget extends VisTable{
	final int slotsize = 50;
	private ItemStack[][] stacks;
	private ItemStack selected;

	public InventoryWidget() {
		stacks = Koru.module(ClientData.class).player.getComponent(InventoryComponent.class).inventory;
		stacks[0][0] = new ItemStack(Item.stone, 1);
		stacks[1][0] = new ItemStack(Item.stone, 1);
		setup();
	}

	void setup(){
		padRight(3f);
		padBottom(3f);
		
		for(int y = 0; y < stacks[0].length; y++){
			for(int x = 0; x < stacks.length; x++){
				Slot slot = new Slot(x,y);
				slot.addListener(new ClickListener(){
					public void clicked(InputEvent event, float x, float y){
						ItemStack stack = stacks[slot.x][slot.y];
						if(selected == null){
							if(stack != null){
								stacks[slot.x][slot.y] = null;
								selected = stack;
							}
						}else{
							if(stack != null){
								stacks[slot.x][slot.y] = selected;
								selected = stack;
							}else{
								stacks[slot.x][slot.y] = selected;
								selected = null;
							}
						}
					}
				});
				add(slot).size(slotsize);
			}
			row();
		}
	}
	
	public void draw(Batch batch, float alpha){
		super.draw(batch, alpha);
		
		if(selected != null)
		batch.draw(Resources.region(selected.item.name() + "item"), 
				Gdx.input.getX() - slotsize/2, (Gdx.graphics.getHeight()-Gdx.input.getY()) - slotsize/2, slotsize, slotsize);
	}
	
	class Slot extends Actor{
		public final int x,y;
		
		public Slot(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public String toString(){
			return "Slot " + x + ", " + y;
		}
		
		public void draw(Batch batch, float alpha){
			batch.setColor(getColor());
			VisUI.getSkin().getPatch("button").draw(batch, getX(), getY(), getWidth(), getHeight());
			VisUI.getSkin().getPatch("border").draw(batch, getX(), getY(), getWidth(), getHeight());
			if(stacks[x][y] != null)
			batch.draw(Resources.region(stacks[x][y].item.name() + "item"), getX(), getY(), getWidth(), getHeight());
		}
	}
}
