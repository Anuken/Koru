package net.pixelstatic.koru.modules;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.network.packets.InputPacket;
import net.pixelstatic.koru.systems.CollisionSystem;
import net.pixelstatic.koru.utils.InputType;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

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
	
		
		float speed = 1f;
		
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
		if(button == Buttons.LEFT){
			sendInput(InputType.leftclick_down);
		}else if(button == Buttons.RIGHT){
			sendInput(InputType.rightclick_down);
		}
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

}
