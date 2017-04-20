package io.anuke.koru.modules;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.SceneModule;
import io.anuke.koru.Koru;
import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.graphics.Cursors;
import io.anuke.koru.ui.*;
import io.anuke.koru.utils.Profiler;
import io.anuke.scene.style.Styles;
import io.anuke.scene.ui.Label;
import io.anuke.scene.ui.TextButton;
import io.anuke.scene.ui.TextField;
import io.anuke.scene.ui.layout.Stack;
import io.anuke.scene.ui.layout.Table;
import io.anuke.scene.utils.ClickListener;
import io.anuke.scene.utils.CursorManager;
import io.anuke.ucore.core.UInput;

public class UI extends SceneModule<Koru> {
	private ObjectMap<Class<?>, Menu> menus = new ObjectMap<>();
	ChatTable chat;
	Table menutable;
	Table chattable;
	Table uitable;
	Label connectlabel;
	Label connectfail;
	Table title;
	Network network;
	Menu currentMenu;

	public void init() {
		network = getModule(Network.class);
	}

	public UI() {
		loadSkin();
		setupMenu();
		setupChat();
		setupUI();
		
		UInput.flipProcessors();
		
		CursorManager.arrow = Cursors.loadCursor("cursor");
		CursorManager.hand = Cursors.loadCursor("hand");
		CursorManager.ibeam = Cursors.loadCursor("ibar");
	}
	
	public void loadSkin(){
		float s = 1f;
		FileHandle skinFile = Gdx.files.internal("ui/uiskin.json");
		styles = new Styles();

		FileHandle atlasFile = skinFile.sibling(skinFile.nameWithoutExtension() + ".atlas");
		if(atlasFile.exists()){
			TextureAtlas atlas = new TextureAtlas(atlasFile);
			try{
				Field field = styles.getClass().getDeclaredField("atlas");
				field.setAccessible(true);
				field.set(styles, atlas);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			styles.addRegions(atlas);
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

		
		styles.add("default-font", font);
		styles.add("smooth-font", font);
		styles.add("large-font", largefont);
		styles.add("border-font", borderfont);
		
		FreeTypeFontGenerator pgenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/prose.ttf"));
		
		FreeTypeFontParameter pixelparameter = new FreeTypeFontParameter();
		pixelparameter.borderStraight = true;
		pixelparameter.mono = true;
		pixelparameter.size = 16;
		pixelparameter.borderWidth = 1;
		pixelparameter.borderColor = new Color(0,0,0,1f);
		pixelparameter.spaceX = -1;
		
		
		FreeTypeFontParameter pixelparameter2 = new FreeTypeFontParameter();
		pixelparameter2.borderStraight = true;
		pixelparameter2.mono = true;
		pixelparameter2.size = 16;
		
		BitmapFont pixelfont = pgenerator.generateFont(pixelparameter);
		pixelfont.getData().setScale(2f);
		styles.add("pixel-font", pixelfont);
		
		BitmapFont pixelfont2 = pgenerator.generateFont(pixelparameter2);
		pixelfont2.getData().setScale(2f);
		pixelfont2.setUseIntegerPositions(false);
		styles.add("pixel-font-noborder", pixelfont2);

		styles.load(skinFile);

		Styles.load(styles);

		generator.dispose();
		pgenerator.dispose();
	}
	
	void setupUI(){
		uitable = scene.table();
		
		Stack stack = new Stack();
		
		uitable.add(stack).grow();
		
		Table invTable = new Table();
		stack.add(invTable);
		
		invTable.bottom().right().add(new InventoryMenu()).align(Align.bottomRight);
		
		Table rtable = new Table();
		rtable.center().add(new RecipeMenu()).padBottom(300f);
		stack.add(rtable);
	}

	void setupMenu() {
		
		menutable = scene.table();
		
		menutable.background("window-noborder");
		menutable.center();
		
		connectfail = new Label("Connection Failed!");
		connectfail.setColor(Color.RED);
		connectfail.setAlignment(Align.center, Align.center);
		connectlabel = new Label("Connecting...");

		TextButton button = new TextButton("Connect");
		
		TextField name = new TextField(System.getProperty("user.name"));
		
		//enter key handling
		name.typed(c->{
			if (c == '\n') {
				((ClickListener) button.getListeners().get(2)).clicked(null, 0, 0);
			}else{
				getModule(ClientData.class).player.getComponent(ConnectionComponent.class).name = name.getText();
			}
		});


		menutable.add(connectfail).colspan(2).padBottom(20).minHeight(100).minWidth(300).row();
		menutable.add(connectlabel).colspan(2).row();
		menutable.add(new Label("Name: ")).padBottom(6f).align(Align.right);
		menutable.add(name).padBottom(6f).row();
		menutable.add(button).colspan(2).fillX().padTop(5).padBottom(200);
		
		button.clicked(()->{
			if (!network.connecting && !network.connected()){
				getModule(ClientData.class).player.getComponent(ConnectionComponent.class).name = name.getText();
				name.getStage().setKeyboardFocus(null);
				network.connect();
			}
		});
		
		title = scene.table();
		
		Label tlabel = new Label("[WHITE]< [SKY]Koru [WHITE]>");
		tlabel.getStyle().font.getData().markupEnabled = true;
		tlabel.setFontScale(2f);
		title.top().add(tlabel).padTop(60f);
	}
	
	void setupChat() {
		chat = new ChatTable();
		chat.setFillParent(true);
		scene.add(chat);
	}
	
	public void updateUIVisibility(){
		connectlabel.setVisible(network.connecting);
		connectfail.setVisible(network.initialconnect && !network.connected());
		connectfail.setText(network.getError());
		menutable.setVisible(!network.connected());
		uitable.setVisible(network.connected());
		title.setVisible(!network.connected());
	}
	
	public boolean mouseOnUI(){
		return scene.hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), true) != null;
	}
	
	public boolean menuOpen(){
		return mouseOnUI() || currentMenu != null || chat.chatOpen();
	}
	
	public void openMenu(Class<? extends Menu> c){
		Menu m = null;
		if(menus.containsKey(c)){
			m = menus.get(c);
		}else{
			try{
				m = c.newInstance();
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			menus.put(c, m);
		}
		
		m.show(scene);
		currentMenu = m;
		m.onOpen();
		
		Cursors.setCursor("cursor");
	}
	
	public void closeMenu(){
		if(currentMenu != null){
			currentMenu.hide();
			currentMenu.onClose();
			currentMenu = null;
		}
	}

	@Override
	public void update() {
		long start = TimeUtils.nanoTime();
		
		updateUIVisibility();
		
		scene.act();
		scene.draw();
		
		
		if(Profiler.update())
			Profiler.uiTime = TimeUtils.timeSinceNanos(start);
	}
}
