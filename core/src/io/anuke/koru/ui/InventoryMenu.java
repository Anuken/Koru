package io.anuke.koru.ui;

import com.badlogic.gdx.Gdx;
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
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.packets.InventoryClickPacket;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.graphics.Hue;

public class InventoryMenu extends VisTable{
	public static final int slotsize = 64;
	private ItemStack[][] stacks;
	private InventoryComponent inventory;

	public InventoryMenu() {
		inventory = Koru.module(ClientData.class).player.getComponent(InventoryComponent.class);
		stacks = inventory.inventory;
		setup();
	}

	void setup(){
		padRight(3f);
		padBottom(3f);

		for(int y = 0; y < stacks[0].length; y++){
			for(int x = 0; x < stacks.length; x++){
				Slot slot = new Slot(x, y);
				slot.addListener(new ClickListener(){
					public void clicked(InputEvent event, float x, float y){
						//inventory.clickSlot(slot.x, slot.y);
						InventoryClickPacket packet = new InventoryClickPacket();
						packet.x = slot.x;
						packet.y = slot.y;
						Koru.module(Network.class).client.sendTCP(packet);
					}
				});
				add(slot).size(slotsize);
			}
			row();
		}
	}

	public void draw(Batch batch, float alpha){
		float pscale = slotsize / 16;

		batch.setColor(Hue.lightness(95 / 255f));
		batch.draw(Resources.region("blank"), getX() - pscale, getY() - pscale, getWidth() + pscale * 2, getHeight() + pscale * 2);
		batch.setColor(Color.WHITE);

		super.draw(batch, alpha);

		if(inventory.selected != null){
			drawItem(batch, alpha, Gdx.input.getX() - slotsize/2, Gdx.graphics.getHeight() - Gdx.input.getY() - slotsize/2, inventory.selected);
		}

		Actor actor = getStage().hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), true);

		if(actor instanceof Slot){
			Slot slot = (Slot) actor;

			if(stacks[slot.x][slot.y] != null){
				Resources.font3().getData().setScale(2f);
				Resources.font3().setColor(Color.YELLOW);
				Resources.font3().draw(batch, stacks[slot.x][slot.y].item.formalName(), Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY() + 17, 0, Align.bottomRight, false);
				Resources.font3().setColor(Color.WHITE);
			}
		}
	}
	
	public static void drawItem(Batch batch, float alpha, float x, float y, ItemStack stack){
		TextureRegion region = Resources.region(stack.item.name() + "item");

		batch.setColor(0, 0, 0, 0.1f*alpha);
		batch.draw(region, x + slotsize / 2 - region.getRegionWidth() / 2 * slotsize / 16, y + slotsize / 2 - region.getRegionHeight() / 2 * slotsize / 16 - 4, region.getRegionWidth() * slotsize / 16, region.getRegionHeight() * slotsize / 16);

		batch.setColor(1f, 1f, 1f, alpha);
		batch.draw(region, x + slotsize / 2 - region.getRegionWidth() / 2 * slotsize / 16, y + slotsize / 2 - region.getRegionHeight() / 2 * slotsize / 16, region.getRegionWidth() * slotsize / 16, region.getRegionHeight() * slotsize / 16);

		Resources.font2().getData().setScale(2f);

		if(stack.amount > 1){

			Resources.font2().setColor(0, 0, 0, 0.15f*alpha);
			Resources.font2().draw(batch, stack.amount + "", x + 8 + 2, y + 54 - 2);
			
			//fixes wierd crash bug?
			if(stack != null){
				Resources.font2().setColor(1, 1, 1, alpha);
				Resources.font2().draw(batch, stack.amount + "", x + 8, y + 54);
			}
		}
	}

	class Slot extends Actor{
		public final int x, y;
		public final ClickListener click;

		public Slot(int x, int y) {
			this.x = x;
			this.y = y;

			addListener((click = new ClickListener()));
		}

		public String toString(){
			return "Slot " + x + ", " + y;
		}

		public void draw(Batch batch, float alpha){
			batch.setColor(getColor());
			batch.draw(Resources.region((y == 0 && x == inventory.hotbar) ? "slotselect" : "slot"), getX(), getY(), getWidth(), getHeight());
			//draw(batch, getX(), getY(), getWidth(), getHeight());
			if(stacks[x][y] != null){
				drawItem(batch, alpha, getX(), getY(), stacks[x][y]);
			}
		}
	}
}
