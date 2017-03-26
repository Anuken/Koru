package io.anuke.koru.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import io.anuke.koru.utils.Resources;

public class CraftingMenu extends Menu{
	
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
		
		left.add("[FIREBRICK]Construct").colspan(2).row();
		
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
		
		table.add(desctable).colspan(2).row();
		
		desctable.left();
		
		desctable.add("Description text line 1...");
		desctable.row();
		desctable.add("Description text line 2, some stats");
		
		Table itemtable = new VisTable();
		
		table.add(itemtable).colspan(2).padTop(5).row();
		
		int slots = 8;
		
		for(int i = 0; i < slots; i ++){
			itemtable.add(new VisImage(Resources.region("slot"))).size(64);
		}
		
		itemtable.row();
		
		for(int i = 0; i < slots; i ++){
			itemtable.add(new VisImage(Resources.region("slot"))).size(64);
		}
	}

	@Override
	public void onOpen(){

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
}
