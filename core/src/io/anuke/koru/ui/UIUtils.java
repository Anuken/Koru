package io.anuke.koru.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.CursorManager;
import com.kotcrab.vis.ui.widget.VisTextField;

import io.anuke.koru.graphics.Cursors;

public class UIUtils{
	
	public static void setCursors(Button button){
		button.addListener(new ClickListener() {
			@Override
			public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (pointer == -1 && button.isDisabled() == false) {
					Gdx.graphics.setCursor(Cursors.loadCursor("hand"));
					Cursors.setCursor("hand");
				}
			}

			@Override
			public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (pointer == -1) {
					CursorManager.restoreDefaultCursor();
					Cursors.setCursor("cursor");
				}
			}
		});
	}
	
	public static void setCursors(VisTextField button){
		button.removeListener(button.getListeners().get(1));
		
		button.addListener(new ClickListener() {
			@Override
			public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (pointer == -1 && button.isDisabled() == false) {
					Gdx.graphics.setCursor(Cursors.loadCursor("ibar"));
				}
			}

			@Override
			public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (pointer == -1) {
					CursorManager.restoreDefaultCursor();
				}
			}
		});
	}
}
