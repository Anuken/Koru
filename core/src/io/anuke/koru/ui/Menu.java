package io.anuke.koru.ui;

import com.kotcrab.vis.ui.widget.VisDialog;

public abstract class Menu extends VisDialog{

	public Menu(String title) {
		super(title);
	}
	
	public abstract void onOpen();
	public abstract void onClose();
}
