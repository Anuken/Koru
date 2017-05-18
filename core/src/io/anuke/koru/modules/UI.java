package io.anuke.koru.modules;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.koru.Koru;
import io.anuke.koru.graphics.KoruCursors;
import io.anuke.koru.modules.Control.GameState;
import io.anuke.koru.ui.*;
import io.anuke.koru.utils.Profiler;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.modules.SceneModule;
import io.anuke.ucore.scene.builders.*;
import io.anuke.ucore.scene.ui.*;
import io.anuke.ucore.scene.utils.Cursors;
import io.anuke.ucore.util.Timers;

public class UI extends SceneModule<Koru> {
	private ObjectMap<Class<?>, Menu> menus = new ObjectMap<>();
	private Menu currentMenu;
	
	private ChatTable chat;
	private TextDialog error;
	private ConnectDialog connect;
	private KeybindDialog keybinds;
	private SettingsDialog settings;
	
	private InventoryMenu inventory;
	private RecipeMenu recipes;
	
	private BooleanSupplier titlevis = ()->{return Koru.control.isState(GameState.title);};
	private BooleanSupplier playvis = ()->{return Koru.control.isPlaying();};

	public UI() {
		skin = StyleLoader.loadStyles();
		Dialog.closePadR -= 1;
		
		Inputs.flipProcessors();
		
		Cursors.arrow = KoruCursors.loadCursor("cursor");
		Cursors.hand = KoruCursors.loadCursor("hand");
		Cursors.ibeam = KoruCursors.loadCursor("ibar");
	}
	
	@Override
	public void init(){
		error = new TextDialog("Error", "N/A");
		error.setDialog();
		connect = new ConnectDialog();
		settings = new SettingsDialog();
		keybinds = new KeybindDialog();
		chat = new ChatTable();
		
		//TODO settings
		settings.checkPref("smoothcam", "Smooth Camera", false);
		
		setup();
		
		//TODO setting to autoshow this
		
		
		Timers.run(20, ()->{
			connect.show();
		});
	}
	
	void setup(){
		build.begin();
		
		//title menu
		new table("window-noborder"){{
			defaults().size(180, 45);
			
			new button("Connect", ()->{
				connect.show();
			});
			
			row();
			
			new button("Settings", ()->{
				settings.show();
			});
			
			row();
			
			new button("Controls", ()->{
				keybinds.show();
			});
		}}.end().visible(titlevis);
		
		//title text
		new table(){{
			atop();
			
			//TODO fix hacky title
			
			new label("[gray]Koru"){{
				get().setFontScale(4f);
			}}.padBottom(-124);
			
			row();
			
			new label("Koru"){{
				get().setFontScale(4f);
			}};
			
		}}.end().visible(titlevis);
		
		//inventory
		new table(){{
			abottom();
			aright();
			
			add(inventory = new InventoryMenu());
		}}.end().visible(playvis);
		
		//recipes
		new table(){{
			add(recipes = new RecipeMenu()).padBottom(300);
		}}.end().visible(playvis);
		
		//debug
		new table(){{
			atop();
			aleft();
			new label(()->{
				return Gdx.graphics.getFramesPerSecond() + " FPS";
			});
		}}.end().visible(playvis);
		
		//chat
		chat.setVisible(playvis);
		scene.add(chat);
		
		build.end();
	}
	
	public void toggleChat(){
		chat.toggle();
		
		if(chat.chatOpen()){
			Koru.control.setState(GameState.chatting);
		}else{
			Koru.control.setState(GameState.playing);
		}
	}
	
	public void hideConnect(){
		connect.hide();
	}
	
	public void handleChatMessage(String message, String sender){
		chat.addMessage(message, sender);
	}
	
	public boolean menuOpen(){
		return hasMouse() || currentMenu != null || chat.chatOpen();
	}
	
	public void showError(String... lines){
		error.set("Error", lines);
		error.show();
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
		
		KoruCursors.setCursor("cursor");
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
		
		scene.act();
		scene.draw();
		
		if(Profiler.update())
			Profiler.uiTime = TimeUtils.timeSinceNanos(start);
	}
}
