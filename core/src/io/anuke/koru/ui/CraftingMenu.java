package io.anuke.koru.ui;

import static io.anuke.koru.ui.InventoryMenu.slotsize;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.CursorManager;
import com.kotcrab.vis.ui.widget.VisTable;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.graphics.Cursors;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.utils.Resources;

public class CraftingMenu extends Menu{
	final int slots = 8;
	private final Slot[] rodslots = new Slot[slots];
	private final Slot[] matslots = new Slot[slots];
	private Slot rodslot;
	private Slot matslot;
	private ItemStack dragged;
	
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
		
		left.add(matslot = new Slot(false)).size(64);
		left.add("Material").padLeft(5f).align(Align.left);
		left.row();
		
		left.add(rodslot = new Slot(false)).size(64);
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
		
		Table matable = new VisTable();
		
		for(int i = 0; i < slots; i ++){
			Slot slot = new Slot(true);
			matslots[i] = slot;
			matable.add(slot).size(slotsize);
		}
		
		itemtable.add("Materials: ").align(Align.left);
		itemtable.row();
		itemtable.add(matable);
		itemtable.row();
		
		Table rodtable = new VisTable();
		
		for(int i = 0; i < slots; i ++){
			Slot slot = new Slot(true);
			rodslots[i] = slot;
			rodtable.add(slot).size(slotsize);
		}
		
		itemtable.add("Handles: ").align(Align.left);
		itemtable.row();
		itemtable.add(rodtable);
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
	
	@Override
	public void draw(Batch batch, float alpha){
		super.draw(batch, alpha);
		
		if(dragged != null){
			float x = Gdx.input.getX() - slotsize/2;
			float y = Gdx.graphics.getHeight() - Gdx.input.getY() - slotsize/2;
			
			float rx = slotsize/2 + (UIUtils.worldX(dragged.isType(ItemType.rod) ? rodslot : matslot));
			float ry = slotsize/2 + (UIUtils.worldY(dragged.isType(ItemType.rod) ? rodslot : matslot));
			
			TextureRegion region = Resources.region(dragged.item.name()+"item");
			
			if(Vector2.dst(x+slotsize/2, y+slotsize/2, rx, ry) < slotsize){
				batch.setColor(1,1,1,alpha*0.5f);
				batch.draw(region, rx-region.getRegionWidth()*2, ry-region.getRegionHeight()*2, region.getRegionWidth()*4, region.getRegionHeight()*4);
				batch.setColor(Color.WHITE);
			}
			
			InventoryMenu.drawItem(batch, alpha, x, y, dragged);
		}
	}
	
	private void onDragDrop(){
		float x = Gdx.input.getX();
		float y = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		float rx = slotsize/2 + (UIUtils.worldX(dragged.isType(ItemType.rod) ? rodslot : matslot));
		float ry = slotsize/2 + (UIUtils.worldY(dragged.isType(ItemType.rod) ? rodslot : matslot));
		
		if(Vector2.dst(x, y, rx, ry) < slotsize){
			if(dragged.isType(ItemType.rod)){
				rodslot.stack = dragged;
			}else{
				matslot.stack = dragged;
			}
		}
	}
	
	class ToolView extends Actor{
		@Override
		public void draw(Batch batch, float alpha){
			
			batch.setColor(1, 1, 1, alpha);
			VisUI.getSkin().getDrawable("slot").draw(batch, getX(), getY(), getWidth(), getHeight());
			
			batch.setColor(1, 1, 1, alpha);
			batch.draw(Resources.region("tooltemplate"), getX(), getY(), getWidth(), getHeight());
			
			batch.setColor(Color.WHITE);
		}
	}
	
	class Slot extends Actor{
		public ClickListener click;
		public ItemStack stack;

		public Slot(boolean draggable) {
			if(draggable)
			addListener(click = new ClickListener() {
				@Override
				public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
					super.enter(event, x, y, pointer, fromActor);
					if(stack != null){
						Gdx.graphics.setCursor(Cursors.loadCursor("hand"));
						Cursors.setCursor("hand");
					}
				}

				@Override
				public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
					super.exit(event, x, y, pointer, toActor);
					CursorManager.restoreDefaultCursor();
					Cursors.setCursor("cursor");
				}
			});
			
			addListener(new InputListener(){
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					if(stack != null){
						dragged = stack;
						return true;
					}
					
					return false;
				}
				
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					if(stack == dragged){
						onDragDrop();
					}
					dragged = null;
				}
			});
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
