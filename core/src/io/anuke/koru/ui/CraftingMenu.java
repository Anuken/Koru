package io.anuke.koru.ui;

import static io.anuke.koru.ui.InventoryMenu.slotsize;
import static io.anuke.scene.style.Styles.styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.items.Tools;
import io.anuke.koru.modules.ClientData;
import io.anuke.koru.utils.Resources;
import io.anuke.scene.Element;
import io.anuke.scene.event.InputEvent;
import io.anuke.scene.event.InputListener;
import io.anuke.scene.ui.ImageButton;
import io.anuke.scene.ui.Label;
import io.anuke.scene.ui.layout.Table;
import io.anuke.scene.utils.ClickListener;
import io.anuke.scene.utils.CursorManager;
import io.anuke.ucore.core.Mathf;

public class CraftingMenu extends Menu{
	final int slots = 8;
	private final Slot[] rodslots = new Slot[slots];
	private final Slot[] matslots = new Slot[slots];
	private Slot rodslot;
	private Slot matslot;
	private ItemStack dragged;
	private int toolSize, toolLength;
	private Label nametext;

	public CraftingMenu() {
		super("Tool Crafting");

		this.setBackground("window-gray");

		addCloseButton();

		Table table = getContentTable();
		table.top();

		Table left = new Table();
		Table right = new Table();

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

		Table desctable = new Table();

		table.add(desctable).padTop(5).colspan(2).row();

		desctable.left();

		desctable.add(nametext = new Label("Type: [SKY]Dagger")).align(Align.left);
		desctable.row();
		desctable.add("Specialization: [YELLOW]Chisel").align(Align.left);

		Table ctable = new Table();

		table.add(ctable).padTop(5).colspan(2).row();

		addStatButtons(ctable);

		Table itemtable = new Table();

		table.add(itemtable).colspan(2).padTop(10).row();

		Table matable = new Table();

		for(int i = 0; i < slots; i++){
			Slot slot = new Slot(true);
			matslots[i] = slot;
			matable.add(slot).size(slotsize);
		}

		itemtable.add("Materials: ").align(Align.left);
		itemtable.row();
		itemtable.add(matable);
		itemtable.row();

		Table rodtable = new Table();

		for(int i = 0; i < slots; i++){
			Slot slot = new Slot(true);
			rodslots[i] = slot;
			rodtable.add(slot).size(slotsize);
		}

		itemtable.add("Handles: ").align(Align.left);
		itemtable.row();
		itemtable.add(rodtable);
	}

	void addStatButtons(Table table){
		String[] titles = {"[CORAL]Tool Size", "[WHITE]Tool Length"};
		
		for(int j = 0; j < 2; j++){
			int index = j;
			
			table.add(titles[j]).colspan(5);
			table.row();
			
			ImageButton l1 = new ImageButton("gray");
			ImageButton l2 = new ImageButton("gray");
			
			l1.getStyle().imageUp = styles.getDrawable("icon-arrow-left");
			l2.getStyle().imageUp = styles.getDrawable("icon-arrow-right");
			
			
			l1.getImageCell().size(32);
			l2.getImageCell().size(32);
			
			l1.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y){
					if(index == 0)
						toolSize --;
					else
						toolLength --;
					
					toolSize = Mathf.clamp(toolSize, 0, 2);
					toolLength = Mathf.clamp(toolLength, 0, 2);
					updateTool();
				}
			});
			
			l2.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y){
					if(index == 0)
						toolSize ++;
					else
						toolLength ++;
					
					toolSize = Mathf.clamp(toolSize, 0, 2);
					toolLength = Mathf.clamp(toolLength, 0, 2);
					updateTool();
				}
			});
			
			table.add(l1).size(48);

			for(int i = 0; i < 3; i++)
				table.add(new StatCheck(i, j)).size(48);

			table.add(l2).size(48);
			
			if(j == 0){
				table.row();
				table.add().height(10);
				table.row();
			}
		}
	}
	
	private void updateTool(){
		nametext.setText("Type: [SKY]" + Tools.getToolName(toolLength, toolSize));
	}
	
	@Override
	public void onOpen(){
		InventoryComponent inv = Koru.module(ClientData.class).player.inventory();
		Array<ItemStack> ar = inv.asArray();

		rodslot.stack = matslot.stack = null;

		for(int i = 0; i < slots; i++)
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
			float x = Gdx.input.getX() - slotsize / 2;
			float y = Gdx.graphics.getHeight() - Gdx.input.getY() - slotsize / 2;

			float rx = slotsize / 2 + ((dragged.isType(ItemType.rod) ? rodslot : matslot).worldPos().x);
			float ry = slotsize / 2 + ((dragged.isType(ItemType.rod) ? rodslot : matslot).worldPos().y);

			TextureRegion region = Resources.region(dragged.item.name() + "item");

			if(Vector2.dst(x + slotsize / 2, y + slotsize / 2, rx, ry) < slotsize){
				batch.setColor(1, 1, 1, alpha * 0.5f);
				batch.draw(region, rx - region.getRegionWidth() * 2, ry - region.getRegionHeight() * 2, region.getRegionWidth() * 4, region.getRegionHeight() * 4);
				batch.setColor(Color.WHITE);
			}

			InventoryMenu.drawItem(batch, alpha, x, y, dragged);
		}
	}

	private void onDragDrop(){
		float x = Gdx.input.getX();
		float y = Gdx.graphics.getHeight() - Gdx.input.getY();

		float rx = slotsize / 2 + ((dragged.isType(ItemType.rod) ? rodslot : matslot).worldPos().x);
		float ry = slotsize / 2 + ((dragged.isType(ItemType.rod) ? rodslot : matslot).worldPos().y);

		if(Vector2.dst(x, y, rx, ry) < slotsize){
			if(dragged.isType(ItemType.rod)){
				rodslot.stack = dragged;
			}else{
				matslot.stack = dragged;
			}
		}
	}

	class ToolView extends Element{
		@Override
		public void draw(Batch batch, float alpha){

			batch.setColor(1, 1, 1, alpha);
			styles.getDrawable("slot").draw(batch, getX(), getY(), getWidth(), getHeight());

			batch.setColor(1, 1, 1, alpha);
			batch.draw(Resources.region("tooltemplate"), getX(), getY(), getWidth(), getHeight());

			batch.setColor(Color.WHITE);
		}
	}

	class StatCheck extends Element{
		int i, j;
		
		public StatCheck(int i, int j){
			this.i = i;
			this.j = j;
		}
		
		@Override
		public void draw(Batch batch, float alpha){
			styles.getDrawable("slot").draw(batch, getX(), getY(), getWidth(), getHeight());
		
			batch.setColor(((j == 0 && i > toolSize) || (j == 1 && i > toolLength)) ? Color.CLEAR : j == 0 ? Color.CORAL : Color.WHITE);
			
			styles.getDrawable("bump").draw(batch, getX(), getY(), getWidth(), getHeight());
			
			batch.setColor(Color.WHITE);
		}
	}

	class Slot extends Element{
		public ClickListener click;
		public ItemStack stack;

		public Slot(boolean draggable) {
			if(draggable)
				addListener(click = new ClickListener(){
					@Override
					public void enter(InputEvent event, float x, float y, int pointer, Element fromActor){
						super.enter(event, x, y, pointer, fromActor);
						if(stack != null){
							CursorManager.setHand();
						}
					}

					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Element toActor){
						super.exit(event, x, y, pointer, toActor);
						CursorManager.restoreCursor();
					}
				});

			addListener(new InputListener(){
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
					if(stack != null){
						dragged = stack;
						return true;
					}

					return false;
				}

				public void touchUp(InputEvent event, float x, float y, int pointer, int button){
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
