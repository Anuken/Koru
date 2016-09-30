package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;

import io.anuke.koru.Koru;
import io.anuke.ucore.modules.Module;

public class UI extends Module<Koru>{
	Stage stage;
	
	public UI(){
		VisUI.load(Gdx.files.internal("ui/uiskin.json"));
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void resize(int width, int height){
		stage.getViewport().update(width, height);
	}
}
