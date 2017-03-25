package io.anuke.koru.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.UI;

public abstract class Menu extends VisDialog{

	public Menu(String title) {
		super(title);
		getTitleTable().padTop(-15);
		getTitleLabel().setColor(Color.YELLOW);
		setMovable(false);
	}
	
	@Override
	public void addCloseButton () {
		Label titleLabel = getTitleLabel();
		Table titleTable = getTitleTable();

		VisImageButton closeButton = new VisImageButton("close-window");
		titleTable.add(closeButton).padRight(-getPadRight() + 0.7f).size(40).padTop(-titleTable.getPadTop()-11);
		closeButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Koru.module(UI.class).closeMenu();
			}
		});
		closeButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.cancel();
				return true;
			}
		});

		if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2)
			titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2);
	}
	
	public abstract void onOpen();
	public abstract void onClose();
}
