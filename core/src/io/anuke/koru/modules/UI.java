package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.koru.Koru;
import io.anuke.koru.graphics.KoruCursors;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.modules.Control.GameState;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.ui.*;
import io.anuke.koru.utils.Profiler;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.function.VisibilityProvider;
import io.anuke.ucore.modules.SceneModule;
import io.anuke.ucore.scene.builders.*;
import io.anuke.ucore.scene.ui.*;
import io.anuke.ucore.scene.ui.layout.Stack;
import io.anuke.ucore.scene.utils.Cursors;

public class UI extends SceneModule{
	private ObjectMap<Class<?>, Menu> menus = new ObjectMap<>();
	private Menu currentMenu;
	
	private ChatView chat;
	private TextDialog error;
	private ConnectDialog connect;
	private KeybindDialog keybinds;
	private SettingsDialog settings;
	
	private InventoryView inventory;
	private BlockView blockrecipes;
	private RecipeView recipes;
	
	private VisibilityProvider titlevis = ()->Koru.control.isState(GameState.title);
	private VisibilityProvider playvis = ()->Koru.control.isPlaying();

	public UI(){
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
		chat = new ChatView();
		
		//TODO settings
		settings.checkPref("smoothcam", "Smooth Camera", false);
		
		setup();
		
		//TODO setting to autoshow this
		
		Timers.run(6, ()->{
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
			
			add(inventory = new InventoryView());
		}}.end().visible(playvis);
		
		//recipes
		new table(){{
			visible(()->Koru.control.player.get(InventoryTrait.class).hotbarStack().isType(ItemType.placer));
			aleft();
			
		}}.end().visible(playvis);
		
		//debug
		new table(){{
			atop();
			aright();
			new label(()->Gdx.graphics.getFramesPerSecond() + " FPS");
		}}.end().visible(playvis);
		
		//crafting
		new table(){{
			atop();
			aleft();
			
			blockrecipes = new BlockView();
			recipes = new RecipeView();
			
			new table("button-window-bg"){{
				get().pad(4);
				ButtonGroup<TextButton> group = new ButtonGroup<>();
				
				group.add(new button("Blocks", "toggle", ()->{
					
				}).fillX().uniformX().get());
				
				group.add(new button("Recipes", "toggle", ()->{
					
				}).fillX().uniformX().get());
				
				Stack stack = new Stack();
				
				stack.add(blockrecipes);
				blockrecipes.setVisible(()->group.getCheckedIndex() == 0);
				
				stack.add(recipes);
				recipes.setVisible(()->group.getCheckedIndex() == 1);
				
				row();
				
				add(stack).colspan(group.getButtons().size).fill();
				
			}}.end();
			
			
			row();
			//add(recipes = new PlaceMenu());
			
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
