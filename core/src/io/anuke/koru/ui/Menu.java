package io.anuke.koru.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.UI;
import io.anuke.scene.ui.Dialog;
import io.anuke.scene.ui.ImageButton;
import io.anuke.scene.ui.Label;
import io.anuke.scene.ui.layout.Table;

public abstract class Menu extends Dialog{

	public Menu(String title) {
		super(title);
		getTitleTable().padTop(-15);
		getTitleTable().padLeft(3);
		getTitleLabel().setColor(Color.YELLOW);
		getContentTable().padBottom(getContentTable().getPadBottom()-2);
		setMovable(false);
	}
	
	public void addCloseButton () {
		Label titleLabel = getTitleLabel();
		Table titleTable = getTitleTable();

		ImageButton closeButton = new ImageButton("close-window");
		
		titleTable.add(closeButton).padRight(-getPadRight() + 3.7f).size(40).padTop(-titleTable.getPadTop()-5);
		
		closeButton.changed(()->{
			Koru.module(UI.class).closeMenu();
		});

		if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2)
			titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2);
	}
	
	public abstract void onOpen();
	public abstract void onClose();
}
