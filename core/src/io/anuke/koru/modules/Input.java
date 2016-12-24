package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.network.packets.InputPacket;
import io.anuke.koru.network.packets.SlotChangePacket;
import io.anuke.koru.systems.CollisionSystem;
import io.anuke.koru.utils.Angles;
import io.anuke.koru.utils.InputType;
import io.anuke.ucore.modules.Module;

public class Input extends Module<Koru> implements InputProcessor {
	private Vector2 vector = new Vector2();
	public CollisionSystem collisions;
	KoruEntity player;
	int blockx, blocky;

	public void init() {
		InputMultiplexer plex = new InputMultiplexer();
		plex.addProcessor(getModule(UI.class).stage);
		plex.addProcessor(this);
		Gdx.input.setInputProcessor(plex);
		player = getModule(ClientData.class).player;
		collisions = new CollisionSystem();
	}

	@Override
	public void update() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) && getModule(Network.class).connected())
			getModule(UI.class).chat.enterPressed();

		if (!getModule(Network.class).connected() || getModule(UI.class).chat.chatOpen()) return;
		
		
		Vector3 vec = getModule(Renderer.class).unproject();
		int nx = World.tile(vec.x), ny = World.tile(vec.y);
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && (nx != blockx || ny != blocky)){
			sendInput(InputType.block_moved, nx, ny);
		}
		
		blockx = nx;
		blocky = ny;
		
		if (Gdx.input.isKeyJustPressed(Keys.R))
			sendInput(InputType.r);

		float speed = (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 25f : 2f) * delta();

		if (Gdx.input.isKeyPressed(Keys.W)) {
			vector.y += speed;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			vector.x -= speed;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			vector.y -= speed;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			vector.x += speed;
		}
		
		RenderComponent render = player.getComponent(RenderComponent.class);
		
		ItemStack stack = player.inventory().hotbarStack();
		
		if(stack != null && stack.item.type() == ItemType.weapon){
			float angle = Angles.mouseAngle(getModule(Renderer.class).camera, player.getX(), player.getY());
			render.direction = 2-(int)((angle-45)/90f);
			if(render.direction == 1) render.direction = 3;
			if(angle > 315 || angle < 45) render.direction = 1;
		}else if(key(Keys.W) || key(Keys.A) || key(Keys.S) || key(Keys.D)){
			render.direction = (key(Keys.D) ? 1 : (key(Keys.A) ? 3 : (key(Keys.S) ? 0 : 2)));
		}

		vector.limit(speed);
		float lastx = player.getX();
		float lasty = player.getY();
		
		collisions.moveEntity(player, vector.x, vector.y);
		
		if(Vector2.dst(lastx, lasty, player.getX(), player.getY()) > 0.05f){
			render.walkframe += delta();
		}else{
			render.walkframe = 0;
		}

		vector.set(0, 0);
	}

	void sendInput(InputType type, Object... params) {
		InputPacket packet = new InputPacket();
		packet.data = params;
		packet.type = type;
		getModule(Network.class).client.sendTCP(packet);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			sendInput(InputType.leftclick_down, blockx, blocky);
		} else if (button == Buttons.RIGHT) {
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
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
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
