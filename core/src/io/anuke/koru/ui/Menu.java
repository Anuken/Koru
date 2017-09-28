package io.anuke.koru.ui;

import static io.anuke.ucore.core.Core.skin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import io.anuke.koru.Koru;
import io.anuke.ucore.scene.ui.Dialog;
import io.anuke.ucore.scene.ui.ImageButton;
import io.anuke.ucore.scene.ui.ImageButton.ImageButtonStyle;
import io.anuke.ucore.scene.ui.Label;
import io.anuke.ucore.scene.ui.layout.Table;

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

		ImageButton closeButton = new ImageButton(skin.get("close-window", ImageButtonStyle.class));
		
		titleTable.add(closeButton).padRight(-getPadRight() + 3.7f).size(40).padTop(-titleTable.getPadTop()-5);
		
		closeButton.changed(()->{
			Koru.ui.closeMenu();
		});

		if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2)
			titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2);
	}
	
	public abstract void onOpen();
	public abstract void onClose();
}
