package io.anuke.koru.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CraftingMenu extends Menu{
	
	public CraftingMenu(){
		super("Crafting");
		
		addCloseButton();
		
		Table table = getContentTable();
		table.add("text").size(500, 300);

	}

	@Override
	public void onOpen(){

	}

	@Override
	public void onClose(){

	}

}
