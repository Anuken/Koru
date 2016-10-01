package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import io.anuke.koru.Koru;
import io.anuke.ucore.modules.Module;

public class UI extends Module<Koru>{
	Stage stage;
	VisTable table;
	
	public UI(){
		VisUI.load(Gdx.files.internal("ui/uiskin.json"));
		stage = new Stage(new ScreenViewport());
		setup();
	}
	
	void setup(){
		table = new VisTable();
		table.setFillParent(true);
		stage.addActor(table);
		//VisLabel label = new VisLabel("text");
		//stage.addActor(label);
		
		VisTextButton button = new VisTextButton("Connect");
		table.center().add(button);
	}
	
	@Override
	public void update() {
		stage.act();
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}
}
