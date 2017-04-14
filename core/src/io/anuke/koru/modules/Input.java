package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.input.InputType;
import io.anuke.koru.input.KeyBindings;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.network.packets.BlockInputPacket;
import io.anuke.koru.network.packets.InputPacket;
import io.anuke.koru.network.packets.SlotChangePacket;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.util.Angles;

public class Input extends Module<Koru>{
	private Vector2 vector = new Vector2();
	KoruEntity player;
	int blockx, blocky;

	public void init() {
		InputMultiplexer plex = new InputMultiplexer();
		plex.addProcessor(getModule(UI.class).stage);
		plex.addProcessor(this);
		Gdx.input.setInputProcessor(plex);
		player = getModule(ClientData.class).player;
	}

	@Override
	public void update() {
		if (Gdx.input.isKeyPressed(KeyBindings.exit)) {
			Gdx.app.exit();
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) && getModule(Network.class).connected())
			getModule(UI.class).chat.enterPressed();

		if (!getModule(Network.class).connected() || getModule(UI.class).menuOpen()) return;
		
		
		Vector3 vec = getModule(Renderer.class).unproject();
		int nx = World.tile(vec.x), ny = World.tile(vec.y);
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && (nx != blockx || ny != blocky)){
			sendInput(InputType.block_moved, nx, ny);
		}
		
		blockx = nx;
		blocky = ny;
		
		vector.set(0, 0);

		float speed = (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 25f : 2.2f);

		if (Gdx.input.isKeyPressed(KeyBindings.move_up)) {
			vector.y += speed;
		}
		if (Gdx.input.isKeyPressed(KeyBindings.move_left)) {
			vector.x -= speed;
		}
		if (Gdx.input.isKeyPressed(KeyBindings.move_down)) {
			vector.y -= speed;
		}
		if (Gdx.input.isKeyPressed(KeyBindings.move_right)) {
			vector.x += speed;
		}
		
		RenderComponent render = player.getComponent(RenderComponent.class);
		
		ItemStack stack = player.inventory().hotbarStack();
		
		if(stack != null && stack.isWeapon()){
			float angle = Angles.mouseAngle(getModule(Renderer.class).camera, player.getX(), player.getY());
			render.direction = 2-(int)((angle-45)/90f);
			if(render.direction == 1) render.direction = 3;
			if(angle > 315 || angle < 45) render.direction = 1;
		}else if(key(KeyBindings.move_up) || key(KeyBindings.move_left) || key(KeyBindings.move_down) || key(KeyBindings.move_right)){
			render.direction = (key(KeyBindings.move_right) ? 1 : (key(KeyBindings.move_left) ? 3 : (key(KeyBindings.move_down) ? 0 : 2)));
		}

		vector.limit(speed);
		player.collider().collider.applyImpulse(vector.x*delta(), vector.y*delta());
		
		if(player.collider().collider.getVelocity().len() > 0.05){
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
		}else if(keycode == KeyBindings.interact){
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
			
			if(player.inventory().hotbarStack() == null || player.inventory().hotbarStack().item.isType(ItemType.material))
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
		InventoryComponent inv = getModule(ClientData.class).player.getComponent(InventoryComponent.class);
		int i = ((inv.hotbar+amount) % 4);
		inv.hotbar = i < 0 ? i + 4 : i;
		
		SlotChangePacket packet = new SlotChangePacket();
		packet.slot = inv.hotbar;
		getModule(Network.class).client.sendTCP(packet);
		
		return false;
	}
	
	boolean key(int key){
		return Gdx.input.isKeyPressed(key);
	}

	public GridPoint2 cursorblock() {
		Vector3 v = getModule(Renderer.class).camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
		return new GridPoint2(World.tile(v.x), World.tile(v.y));
	}

}
