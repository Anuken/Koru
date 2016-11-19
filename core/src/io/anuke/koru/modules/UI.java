package io.anuke.koru.modules;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

import io.anuke.koru.Koru;
import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.ui.ChatTable;
import io.anuke.koru.ui.InventoryWidget;
import io.anuke.ucore.modules.Module;

public class UI extends Module<Koru> {
	Stage stage;
	ChatTable chat;
	VisTable menutable;
	VisTable chattable;
	VisTable uitable;
	VisLabel connectlabel;
	VisLabel connectfail;
	Network network;

	public void init() {
		network = getModule(Network.class);
	}

	public UI() {
		loadSkin();
		stage = new Stage(new ScreenViewport());
		setupMenu();
		setupChat();
		setupUI();
	}
	
	public void loadSkin(){
		float s = 1f;
		FileHandle skinFile = Gdx.files.internal("ui/uiskin.json");
		Skin skin = new Skin();

		FileHandle atlasFile = skinFile.sibling(skinFile.nameWithoutExtension() + ".atlas");
		if(atlasFile.exists()){
			TextureAtlas atlas = new TextureAtlas(atlasFile);
			try{
				Field field = skin.getClass().getDeclaredField("atlas");
				field.setAccessible(true);
				field.set(skin, atlas);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			skin.addRegions(atlas);
		}
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/smooth.ttf"));
		
		FreeTypeFontParameter normalparameter = new FreeTypeFontParameter();
		normalparameter.size = (int)(22 * s);

		FreeTypeFontParameter largeparameter = new FreeTypeFontParameter();
		largeparameter.size = (int)(26 * s);

		FreeTypeFontParameter borderparameter = new FreeTypeFontParameter();
		borderparameter.size = (int)(26 * s);
		borderparameter.borderWidth = 2*s;
		borderparameter.borderColor = Color.BLACK;
		borderparameter.spaceX = -2;

		BitmapFont font = generator.generateFont(normalparameter);
		BitmapFont largefont = generator.generateFont(largeparameter);
		BitmapFont borderfont = generator.generateFont(borderparameter);

		skin.add("default-font", font);
		skin.add("smooth-font", font);
		skin.add("large-font", largefont);
		skin.add("border-font", borderfont);
		
		FreeTypeFontGenerator pgenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/prose.ttf"));
		FreeTypeFontParameter pixelparameter = new FreeTypeFontParameter();
		pixelparameter.borderStraight = true;
		pixelparameter.mono = true;
		pixelparameter.size = 16;
		pixelparameter.borderWidth = 1;
		pixelparameter.borderColor = new Color(0,0,0,1);
		pixelparameter.spaceX = -1;
		
		BitmapFont pixelfont = pgenerator.generateFont(pixelparameter);
		pixelfont.getData().setScale(2f);
		skin.add("pixel-font", pixelfont);

		skin.load(skinFile);

		VisUI.load(skin);

		generator.dispose();
		pgenerator.dispose();
	}
	
	void setupUI(){
		uitable = new VisTable();
		uitable.setFillParent(true);
		stage.addActor(uitable);
		
		uitable.bottom().right().add(new InventoryWidget()).align(Align.bottomRight);
	}

	void setupMenu() {
		menutable = new VisTable();
		menutable.setFillParent(true);
		stage.addActor(menutable);
		menutable.background("window");
		menutable.center();
		
		connectfail = new VisLabel("Connection Failed!");
		connectfail.setColor(Color.RED);
		connectfail.setAlignment(Align.center, Align.center);
		connectlabel = new VisLabel("Connecting...");

		VisTextButton button = new VisTextButton("Connect");
		VisTextField name = new VisTextField(System.getProperty("user.name"));
		// enter handling
		name.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(VisTextField textField, char c) {
				if (c == '\n') {
					((ClickListener) button.getListeners().get(2)).clicked(null, 0, 0);
				}else{
					getModule(ClientData.class).player.getComponent(ConnectionComponent.class).name = name.getText();
				}
			}
		});

		menutable.add(connectfail).colspan(2).padBottom(20).row();
		menutable.add(connectlabel).colspan(2).padBottom(20).row();
		menutable.add(new VisLabel("Name: "));
		menutable.add(name).row();
		menutable.add(button).colspan(2).fillX().padTop(5);

		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!network.connecting && !network.connected()){
					getModule(ClientData.class).player.getComponent(ConnectionComponent.class).name = name.getText();
					name.getStage().setKeyboardFocus(null);
					network.connect();
				}
			}
		});
	}
	
	void setupChat() {
		chat = new ChatTable();
		stage.addActor(chat);
		chat.setFillParent(true);
	}
	
	public void updateUIVisibility(){
		connectlabel.setVisible(network.connecting);
		connectfail.setVisible(network.initialconnect && !network.connected());
		connectfail.setText(network.getError());
		menutable.setVisible(!network.connected());
		uitable.setVisible(network.connected());
	}

	@Override
	public void update() {
		updateUIVisibility();
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
