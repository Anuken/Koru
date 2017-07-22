package io.anuke.koru.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

import io.anuke.koru.Koru;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.packets.InventoryClickPacket;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.scene.utils.ClickListener;

public class InventoryMenu extends Table{
	public static final int slotsize = 64;
	private ItemStack[] stacks;
	private InventoryTrait inventory;

	public InventoryMenu() {
		inventory = Koru.control.player.get(InventoryTrait.class);
		stacks = inventory.inventory;
		setup();
	}

	void setup(){
		padRight(3f);
		padBottom(3f);

		int width = 6;

		//TODO fix
		for(int i = 0; i < stacks.length; i++){
			if(i % width == 0)
				row();

			Slot slot = new Slot(i);
			slot.clicked(() -> {
				InventoryClickPacket packet = new InventoryClickPacket();
				packet.index = slot.index;
				Koru.module(Network.class).client.sendTCP(packet);
			});
			add(slot).size(slotsize);

		}
	}

	public void draw(Batch batch, float alpha){
		float pscale = slotsize / 16;

		batch.setColor(Hue.lightness(95 / 255f));
		batch.draw(Draw.region("blank"), getX() - pscale, getY() - pscale, getWidth() + pscale * 2, getHeight() + pscale * 2);
		batch.setColor(Color.WHITE);

		super.draw(batch, alpha);

		if(inventory.selected != null){
			drawItem(batch, alpha, Gdx.input.getX() - slotsize / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - slotsize / 2, inventory.selected);
		}

		Element actor = getScene().hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), true);

		if(actor instanceof Slot){
			Slot slot = (Slot) actor;

			if(stacks[slot.index] != null){
				Resources.font3().getData().setScale(2f);
				Resources.font3().setColor(Color.YELLOW);
				Resources.font3().draw(batch, stacks[slot.index].item.formalName(), Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY() + 17, 0, Align.bottomRight, false);
				Resources.font3().setColor(Color.WHITE);
			}
		}
	}

	public static void drawItem(Batch batch, float alpha, float x, float y, ItemStack stack){
		TextureRegion region = Draw.region(stack.item.name() + "item");

		batch.setColor(0, 0, 0, 0.1f * alpha);
		batch.draw(region, x + slotsize / 2 - region.getRegionWidth() / 2 * slotsize / 16, y + slotsize / 2 - region.getRegionHeight() / 2 * slotsize / 16 - 4, region.getRegionWidth() * slotsize / 16, region.getRegionHeight() * slotsize / 16);

		batch.setColor(1f, 1f, 1f, alpha);
		batch.draw(region, x + slotsize / 2 - region.getRegionWidth() / 2 * slotsize / 16, y + slotsize / 2 - region.getRegionHeight() / 2 * slotsize / 16, region.getRegionWidth() * slotsize / 16, region.getRegionHeight() * slotsize / 16);

		Resources.font2().getData().setScale(2f);

		if(stack.amount > 1){

			Resources.font2().setColor(0, 0, 0, 0.15f * alpha);
			Resources.font2().draw(batch, stack.amount + "", x + 8 + 2, y + 54 - 2);

			//fixes wierd crash bug?
			if(stack != null){
				Resources.font2().setColor(1, 1, 1, alpha);
				Resources.font2().draw(batch, stack.amount + "", x + 8, y + 54);
			}
		}
	}

	class Slot extends Element{
		public final int index;
		public final ClickListener click;

		public Slot(int index) {
			this.index = index;

			addListener((click = new ClickListener()));
		}

		public String toString(){
			return "Slot " + x + ", " + y;
		}

		public void draw(Batch batch, float alpha){
			batch.setColor(1, 1, 1, alpha);
			batch.draw(Draw.region((y == 0 && x == inventory.hotbar) ? "slotselect" : "slot"), getX(), getY(), getWidth(), getHeight());
			//draw(batch, getX(), getY(), getWidth(), getHeight());
			if(stacks[index] != null){
				drawItem(batch, alpha, getX(), getY(), stacks[index]);
			}
		}
	}
}
