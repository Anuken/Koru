package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.Prototypes;
import io.anuke.koru.graphics.lsystems.LSystems;
import io.anuke.koru.input.InputHandler;
import io.anuke.koru.input.InputType;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.network.Net;
import io.anuke.koru.network.packets.*;
import io.anuke.koru.traits.ConnectionTrait;
import io.anuke.koru.traits.DirectionTrait;
import io.anuke.koru.traits.DirectionTrait.Direction;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.UCore;
import io.anuke.ucore.core.*;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.extend.traits.TileCollideTrait;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.util.Angles;

public class Control extends Module{
	public final Spark player;
	public boolean debug = false, consoleOpen = false;
	
	private float movespeed = 1.6f, dashspeed = 18f;
	private int blockx, blocky;
	private GameState state = GameState.title;
	
	public Control(){
		player = new Spark(Prototypes.player);
		player.get(ConnectionTrait.class).name = "you";
		player.get(ConnectionTrait.class).local = true;
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

		if (!canMove() || player.getBasis() == null) return;
		
		Vector2 vec = Graphics.mouseWorld();
		
		int nx = World.tile(vec.x), ny = World.tile(vec.y);
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && (nx != blockx || ny != blocky)){
			sendInput(InputType.block_moved, nx, ny);
		}
		
		blockx = nx;
		blocky = ny;
		
		vector.set(0, 0);

		float speed = (Inputs.keyDown("dash") ? dashspeed : movespeed);
		DirectionTrait dc = player.get(DirectionTrait.class);
		
		if (Inputs.keyDown("up")) {
			vector.y += speed;
			dc.direction = Direction.back;
		}
		
		if (Inputs.keyDown("down")) {
			vector.y -= speed;
			dc.direction = Direction.front;
		}
		
		if (Inputs.keyDown("left")) {
			vector.x -= speed;
			dc.direction = Direction.left;
		}
		
		if (Inputs.keyDown("right")) {
			vector.x += speed;
			dc.direction = Direction.right;
		}
		
		ItemStack stack = player.get(InventoryTrait.class).hotbarStack();
		
		if(stack != null && stack.isType(ItemType.weapon)){
			float angle = Angles.mouseAngle(Core.camera, player.pos().x, player.pos().y);
			
			dc.setOrdinal((int)((angle-45f)/90f));
			
			if(angle > 360-45){
				dc.direction = Direction.right;
			}
		}

		vector.limit(speed);
		player.get(TileCollideTrait.class).move(player, vector.x*delta(), vector.y*(delta()));
		
		if(vector.len() > 0.05){
			dc.walktime += delta();
		}else{
			dc.walktime = 0;
		}

		vector.set(0, 0);
	}
	
	public boolean keyDown (int keycode) {
		
		if(keycode == Keys.P){
			sendBlock();
		}
		
		if(keycode == Keys.T){
			BlockQueryPacket p = new BlockQueryPacket();
			p.x = blockx;
			p.y = blocky;
			Net.send(p);
			Tile tile = Koru.world.getTile(blockx, blocky);
			if(tile != null){
				int variant = tile.rand(LSystems.variants);
				Koru.log("Tile variant: " + (variant-1));
				UCore.log("Code: " + LSystems.test[variant-1].getData().rules);
			}
		}
		
		if(keycode == Keys.R){
			LSystems.generate();
		}
		
		//TODO keybindings for this as well
		if(keycode >= Keys.NUM_1 && keycode < Keys.NUM_5){
			InventoryTrait inv = player.get(InventoryTrait.class);
			
			inv.hotbar = keycode - Keys.NUM_1;
			
			SlotChangePacket packet = new SlotChangePacket();
			packet.slot = inv.hotbar;
			Net.send(packet);
			
		}else if(keycode == KeyBinds.get("interact")){
			sendInput(InputType.interact, blockx, blocky);
		}
		
		return false;
	}

	void sendInput(InputType type, Object... params) {
		InputPacket packet = new InputPacket();
		packet.data = params;
		packet.type = type;
		Net.send(packet);
	}
	
	void sendBlock(){
		Material[] types = {Materials.ltest, Materials.birch, Materials.deadbush, Materials.droopy};
		
		BlockInputPacket p = new BlockInputPacket();
		p.x = blockx;
		p.y = blocky;
		p.material = types[player.get(InventoryTrait.class).hotbar].id();
		Net.send(p);
	}
	
	boolean playerReachesBlock(){
		return Vector2.dst(World.world(cursorX()), World.world(cursorY()), player.pos().x, player.pos().y) < InputHandler.reach;
	}

	int cursorX(){
		return World.tile(Graphics.mouseWorld().x);
	}

	int cursorY(){
		return World.tile(Graphics.mouseWorld().y);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		InventoryTrait inv = player.get(InventoryTrait.class);
		
		if (button == Buttons.LEFT){
			
			sendInput(InputType.leftclick_down, blockx, blocky);
			
			if(inv.hotbarStack() == null || !(inv.hotbarStack().item.isType(ItemType.tool) || inv.hotbarStack().item.isType(ItemType.weapon)))
				sendInput(InputType.interact, blockx, blocky);
		
		}else if (button == Buttons.RIGHT){
			inv.recipe = -1;
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
		InventoryTrait inv = player.get(InventoryTrait.class);
		
		int i = ((inv.hotbar+amount) % 4);
		inv.hotbar = i < 0 ? i + 4 : i;
		
		SlotChangePacket packet = new SlotChangePacket();
		packet.slot = inv.hotbar;
		Net.send(packet);
		
		return false;
	}
	
	public enum GameState{
		title, playing, menu, chatting
	}
}
