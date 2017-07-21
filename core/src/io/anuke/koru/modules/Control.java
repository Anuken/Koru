package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryTrait;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.types.Player;
import io.anuke.koru.input.InputType;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.network.packets.BlockInputPacket;
import io.anuke.koru.network.packets.InputPacket;
import io.anuke.koru.network.packets.SlotChangePacket;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.core.*;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.util.Angles;

public class Control extends Module<Koru>{
	public final KoruEntity player;
	public boolean debug = false, consoleOpen = false;
	
	private float movespeed = 1.6f, dashspeed = 18f;
	private Vector2 vector = new Vector2();
	private int blockx, blocky;
	private GameState state = GameState.title;
	
	public Control(){
		player = new KoruEntity(Player.class);
		player.connection().name = "this player";
		player.connection().local = true;
	}

	@Override
	public void init() {
		Inputs.addProcessor(this);
		
		KeyBinds.defaults(
			"up", Keys.W, 
			"down", Keys.S, 
			"left", Keys.A, 
			"right", Keys.D,
			"dash", Keys.SHIFT_LEFT,
			"chat", Keys.ENTER,
			"interact", Keys.Q,
			"build", Keys.F,
			"exit", Keys.ESCAPE,
			"debug", Keys.F3,
			"log", Keys.GRAVE
		);
		
		Settings.loadAll("io.anuke.koru");
	}
	
	public boolean canMove(){
		return state == GameState.playing;
	}
	
	public boolean isPlaying(){
		return state != GameState.title;
	}
	
	public boolean isState(GameState state){
		return this.state == state;
	}
	
	protected void setState(GameState state){
		this.state = state;
	}

	@Override
	public void update() {
		if (Inputs.keyUp("exit")) {
			Gdx.app.exit();
		}
		
		if(Inputs.keyUp("chat") && isPlaying()){
			Koru.ui.toggleChat();
		}
		
		if(Inputs.keyUp("log")){
			consoleOpen = !consoleOpen;
		}
		
		if(Inputs.keyUp("debug")){
			debug = !debug;
		}

		if (!canMove()) return;
		
		Vector2 vec = Graphics.mouseWorld();
		
		int nx = World.tile(vec.x), ny = World.tile(vec.y);
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && (nx != blockx || ny != blocky)){
			sendInput(InputType.block_moved, nx, ny);
		}
		
		blockx = nx;
		blocky = ny;
		
		vector.set(0, 0);
		
		RenderComponent render = player.getComponent(RenderComponent.class);

		float speed = (Inputs.keyDown("dash") ? dashspeed : movespeed);
		int direction = render.direction;
		
		if (Inputs.keyDown("up")) {
			vector.y += speed;
			direction = 2;
		}
		
		if (Inputs.keyDown("down")) {
			vector.y -= speed;
			direction = 0;
		}
		
		if (Inputs.keyDown("left")) {
			vector.x -= speed;
			direction = 3;
		}
		
		if (Inputs.keyDown("right")) {
			vector.x += speed;
			direction = 1;
		}
		
		render.direction = direction;
		
		ItemStack stack = player.inventory().hotbarStack();
		
		if(stack != null && stack.isType(ItemType.weapon)){
			float angle = Angles.mouseAngle(getModule(Renderer.class).camera, player.getX(), player.getY());
			render.direction = 2-(int)((angle-45)/90f);
			if(render.direction == 1) render.direction = 3;
			if(angle > 315 || angle < 45) render.direction = 1;
		}

		vector.limit(speed);
		player.collider().move(player, vector.x*delta(), vector.y*(delta()));
		
		if(vector.len() > 0.05){
			render.walkframe += delta();
		}else{
			render.walkframe = 0;
		}

		vector.set(0, 0);
	}
	
	public boolean keyDown (int keycode) {
		
		//TODO keybindings for this as well
		if(keycode >= Keys.NUM_1 && keycode < Keys.NUM_5){
			player.inventory().hotbar = keycode - Keys.NUM_1;
			SlotChangePacket packet = new SlotChangePacket();
			packet.slot = player.inventory().hotbar;
			getModule(Network.class).client.sendTCP(packet);
		}else if(keycode == KeyBinds.get("interact")){
			sendInput(InputType.interact, blockx, blocky);
		}
		
		return false;
	}

	void sendInput(InputType type, Object... params) {
		InputPacket packet = new InputPacket();
		packet.data = params;
		packet.type = type;
		getModule(Network.class).client.sendTCP(packet);
	}
	
	void sendBlock(){
		BlockInputPacket p = new BlockInputPacket();
		p.x = blockx;
		p.y = blocky;
		p.material = Materials.grassblock.id();
		getModule(Network.class).client.sendTCP(p);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		if (button == Buttons.LEFT){
			sendInput(InputType.leftclick_down, blockx, blocky);
			
			if(player.inventory().hotbarStack() == null || !(player.inventory().hotbarStack().item.isType(ItemType.tool) || player.inventory().hotbarStack().item.isType(ItemType.weapon)))
				sendInput(InputType.interact, blockx, blocky);
		
		}else if (button == Buttons.RIGHT){
			sendInput(InputType.rightclick_down);
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			sendInput(InputType.leftclick_up);
		} else if (button == Buttons.RIGHT) {
			sendInput(InputType.rightclick_up);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		InventoryTrait inv = player.getComponent(InventoryTrait.class);
		int i = ((inv.hotbar+amount) % 4);
		inv.hotbar = i < 0 ? i + 4 : i;
		
		SlotChangePacket packet = new SlotChangePacket();
		packet.slot = inv.hotbar;
		getModule(Network.class).client.sendTCP(packet);
		
		return false;
	}
	
	public enum GameState{
		title, playing, menu, chatting
	}
}
