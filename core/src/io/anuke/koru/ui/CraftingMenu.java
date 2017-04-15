package io.anuke.koru.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.utils.Resources;

public class CraftingMenu extends Menu{
	final int slots = 8;
	private final Slot[] rodslots = new Slot[slots];
	private final Slot[] matslots = new Slot[slots];
	
	public CraftingMenu(){
		super("Tool Crafting");
		
		this.setBackground("window-gray");
		
		addCloseButton();
		
		Table table = getContentTable();
		table.top();
		
		Table left = new VisTable();
		Table right = new VisTable();
		
		table.add(left).grow();
		table.add(right).grow();
		
		left.add("Construct").colspan(2).row();
		
		left.add(new VisImage(Resources.region("slot"))).size(64);
		left.add("Material").padLeft(5f).align(Align.left);
		left.row();
		
		left.add(new VisImage(Resources.region("slot"))).size(64);
		left.add("Handle").padLeft(5f).align(Align.left);
		
		left.row();
		left.add().grow();
		
		right.add("Tool");
		right.row();
		right.add(new ToolView()).size(200);
		
		table.row();
		
		Table desctable = new VisTable();
		
		table.add(desctable).padTop(5).colspan(2).row();
		
		desctable.left();
		
		desctable.add("Type: [CORAL]Pickaxe").align(Align.left);
		desctable.row();
		desctable.add("Specialization: [YELLOW]Chisel").align(Align.left);
		
		Table itemtable = new VisTable();
		
		table.add(itemtable).colspan(2).padTop(10).row();
		
		for(int i = 0; i < slots; i ++){
			Slot slot = new Slot();
			matslots[i] = slot;
			itemtable.add(slot).size(64);
		}
		
		itemtable.row();
		
		for(int i = 0; i < slots; i ++){
			Slot slot = new Slot();
			rodslots[i] = slot;
			itemtable.add(slot).size(64);
		}
	}

	@Override
	public void onOpen(){
		InventoryComponent inv = Koru.module(ClientData.class).player.inventory();
		Array<ItemStack> ar = inv.asArray();
		
		for(int i = 0; i < slots; i ++)
			rodslots[i].stack = matslots[i].stack = null;
		
		int irod = 0, imat = 0;
		for(ItemStack stack : ar){
			
			if(stack.isType(ItemType.rod))
				rodslots[irod++].stack = stack;
			
			if(stack.isType(ItemType.material))
				matslots[imat++].stack = stack;
		}
	}

	@Override
	public void onClose(){

	}
	
	class ToolView extends Actor{
		@Override
		public void draw(Batch batch, float alpha){
			
			batch.setColor(1, 1, 1, alpha);
			VisUI.getSkin().getDrawable("slot").draw(batch, getX(), getY(), getWidth(), getHeight());
			
			//VisUI.getSkin().getDrawable("border-white").draw(batch, getX(), getY(), getWidth(), getHeight());
			
			batch.setColor(1, 1, 1, alpha);
			batch.draw(Resources.region("tooltemplate"), getX(), getY(), getWidth(), getHeight());
			
			batch.setColor(Color.WHITE);
		}
	}
	
	class Slot extends Actor{
		public final ClickListener click;
		public ItemStack stack;

		public Slot() {
			addListener((click = new ClickListener()));
		}

		public void draw(Batch batch, float alpha){
			batch.setColor(1, 1, 1, alpha);
			batch.draw(Resources.region("slot"), getX(), getY(), getWidth(), getHeight());
			
			if(stack != null){
				InventoryMenu.drawItem(batch, alpha, getX(), getY(), stack);
			}
		}
	}
}
