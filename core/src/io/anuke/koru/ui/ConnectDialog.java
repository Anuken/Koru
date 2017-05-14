package io.anuke.koru.ui;

import io.anuke.koru.Koru;
import io.anuke.ucore.scene.builders.*;
import io.anuke.ucore.scene.ui.Dialog;
import io.anuke.ucore.scene.ui.TextDialog;

public class ConnectDialog extends Dialog{
	TextDialog fail;
	
	public ConnectDialog(){
		super("Connect");
		setup();
	}
	
	private void setup(){
		content().pad(16);
		addCloseButton();
		
		build.begin(content());
		
		new label("Name:");
		
		new field(System.getProperty("user.name"), s->{
			Koru.control.player.connection().name = s;
		}){{get().change();}};
		
		content().row();
		
		new label("IP:");
		
		new field("localhost", s->{
			Koru.network.ip = s;
		});
		
		content().row();
		
		new button("Connect", ()->{
			hide();
			getScene().setKeyboardFocus(null);
			Koru.network.connect();
		}).colspan(2).padTop(10).size(300, 50);
		
		build.end();
	}
}
