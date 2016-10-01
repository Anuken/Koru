package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import io.anuke.koru.Koru;
import io.anuke.ucore.modules.Module;

public class UI extends Module<Koru>{
	Stage stage;
	VisTable table;
	VisLabel connectlabel;
	VisLabel connectfail;
	Network network;
	
	public void init(){
		network = getModule(Network.class);
	}
	
	public UI(){
		VisUI.load(Gdx.files.internal("ui/uiskin.json"));
		stage = new Stage(new ScreenViewport());
		setup();
	}
	
	void setup(){
		table = new VisTable();
		table.setFillParent(true);
		stage.addActor(table);
		table.background("window");
		table.center();
		
		connectfail = new VisLabel("Connection Failed!");
		connectfail.setColor(Color.RED);
		
		table.add(connectfail).colspan(2).padBottom(20).row();
		
		connectlabel = new VisLabel("Connecting...");
		table.add(connectlabel).colspan(2).padBottom(20).row();
		table.add(new VisLabel("Name: "));
		VisTextField name = new VisTextField();
		table.add(name).row();
		
		VisTextButton button = new VisTextButton("Connect");
		table.add(button).colspan(2).fillX().padTop(5);
		
		button.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				network.connect();
			}
		});
	}
	
	@Override
	public void update() {
		connectlabel.setVisible(network.connecting);
		connectfail.setVisible(network.initialconnect && !network.connected());
		table.setVisible(!network.connected());
		stage.act();
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}
}
