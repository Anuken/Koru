package net.pixelstatic.koru.client;

import java.io.IOException;

import net.pixelstatic.koru.network.IClient;
import net.pixelstatic.koru.network.NetworkListener;

import com.badlogic.gdx.utils.Json;
import com.sksamuel.gwt.websockets.CloseEvent;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;

public class WebClient extends IClient{
	Websocket socket;
	NetworkListener listener;
	Json json = new Json();

	@Override
	public void sendTCP(Object object){
		socket.send(json.toJson(object));
	}

	@Override
	public void sendUDP(Object object){
		sendTCP(object);
	}

	@Override
	public void connect(String ip, int port) throws IOException{
		socket = new Websocket("ws://127.0.0.1:" + (port+1));
		socket.open();
		socket.addListener(new WebsocketListener(){
			@Override
			public void onClose(CloseEvent event){
				
			}
			@Override
			public void onMessage(String msg){
				listener.received(json.fromJson(null, msg));
			}

			@Override
			public void onOpen(){
				
			}
			
		});
	}

	@Override
	public void addListener(NetworkListener listener){
		this.listener = listener;
	}

}
