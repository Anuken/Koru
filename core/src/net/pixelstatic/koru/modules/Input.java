package net.pixelstatic.koru.modules;

import java.awt.Point;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.network.packets.BlockInputPacket;
import net.pixelstatic.koru.network.packets.InputPacket;
import net.pixelstatic.koru.network.packets.StoreItemPacket;
import net.pixelstatic.koru.systems.CollisionSystem;
import net.pixelstatic.koru.utils.InputType;
import net.pixelstatic.koru.world.InventoryTileData;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.Tile;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Input extends Module implements InputProcessor{
	private Vector2 vector = new Vector2();
	CollisionSystem collisions;
	KoruEntity player;

	public Input(Koru k){
		super(k);
	}
	
	public void init(){
		Gdx.input.setInputProcessor(this);
		player = getModule(ClientData.class).player;
		collisions = new CollisionSystem();
	}

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.R)) sendInput(InputType.r);
		
		float speed = 2f;
		
		if(Gdx.input.isKeyPressed(Keys.W)){
			vector.y += speed;
		}
		if(Gdx.input.isKeyPressed(Keys.A)){
			vector.x -= speed;
		}
		if(Gdx.input.isKeyPressed(Keys.S)){
			vector.y -= speed;
		}
		if(Gdx.input.isKeyPressed(Keys.D)){
			vector.x += speed;
		}
		
		vector.limit(speed);
		
		if(!collisions.checkTerrainCollisions(getModule(World.class), player, vector.x, 0)){
			player.position().add(vector.x, 0);
		}
		
		if(!collisions.checkTerrainCollisions(getModule(World.class), player, 0, vector.y)){
			player.position().add(0, vector.y);
		}
		
		vector.set(0,0);
		
		if(Gdx.input.isKeyJustPressed(Keys.T)){
			Point point =cursorblock();
			Tile tile = getModule(World.class).getTile(point);
			if(tile.blockdata != null && tile.blockdata instanceof InventoryTileData){
				StoreItemPacket packet = new StoreItemPacket();
				packet.x = point.x;
				packet.y = point.y;
				packet.stack = new ItemStack(Item.wood, 20);
				getModule(Network.class).client.sendTCP(packet);
			}
		}
	}
	
	void sendInput(InputType type){
		InputPacket packet = new InputPacket();
		packet.type = type;
		getModule(Network.class).client.sendTCP(packet);
	}

	@Override
	public boolean keyDown(int keycode){
		return false;
	}

	@Override
	public boolean keyUp(int keycode){
		return false;
	}

	@Override
	public boolean keyTyped(char character){
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		BlockInputPacket packet = new BlockInputPacket();
		if(button == Buttons.LEFT){
			sendInput(InputType.leftclick_down);
			packet.material = Material.woodblock;
		}else if(button == Buttons.RIGHT){
			sendInput(InputType.rightclick_down);
			packet.material = Material.air;
		}
		
		Point mouse = cursorblock();
		packet.x = mouse.x;
		packet.y = mouse.y;
		getModule(Network.class).client.sendTCP(packet);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		if(button == Buttons.LEFT){
			sendInput(InputType.leftclick_up);
		}else if(button == Buttons.RIGHT){
			sendInput(InputType.rightclick_up);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY){
		return false;
	}

	@Override
	public boolean scrolled(int amount){
		return false;
	}
	
	public Point cursorblock(){
		Vector3 v = getModule(Renderer.class).camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(), 1f));
		return new Point(World.tile(v.x), World.tile(v.y));
	}

}
