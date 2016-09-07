package net.pixelstatic.koruserver;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebServer extends WebSocketServer implements Runnable{
	final KoruServer server;
	final Thread sendThread = new Thread(this, "WebServer Thread");

	public WebServer(KoruServer server, InetSocketAddress address){
		super(address);
		this.server = server;
	}
	
	public void run(){
		while(true){
			
			try{
				Thread.sleep(9999999999L);
			}catch (Exception e){
				System.out.println("Interrupted!");
			}
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake){
		
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote){
		
	}

	@Override
	public void onMessage(WebSocket conn, String message){
		//byte[] data = message.getBytes();
	}

	@Override
	public void onError(WebSocket conn, Exception ex){
		
	}

}
