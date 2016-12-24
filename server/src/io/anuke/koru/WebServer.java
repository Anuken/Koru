package io.anuke.koru;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.badlogic.gdx.utils.AtomicQueue;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

import io.anuke.koru.network.packets.ChunkRequestPacket;
import io.anuke.koru.network.packets.ConnectPacket;
import io.anuke.koru.network.packets.PositionPacket;

/**this is a dead class*/
public class WebServer extends WebSocketServer{
	final KoruServer server;
	final Thread sendThread = new Thread(new Runner(), "WebServer Thread");
	AtomicQueue<Request> requests = new AtomicQueue<Request>(1000);
	Json json = new Json();

	public WebServer(KoruServer server, InetSocketAddress address){
		super(address);
		this.server = server;
		this.start();
		sendThread.setDaemon(true);
		sendThread.start();
		Koru.log("Started web server.");
	}

	public static class Request implements Poolable{
		WebSocket socket;
		Object object;
		
		public Request(){
			
		}

		public Request set(WebSocket socket, Object object){
			this.socket = socket;
			this.object = object;
			return this;
		}

		public void reset(){
			object = null;
			socket = null;
		}
	}

	class Runner implements Runnable{
		public void run(){
			while(true){
				Request request = null;
				while((request = requests.poll()) != null){
					String string = "this doesn't work"; //it really doesn't
					request.socket.send(string);
				}
				try{
					Thread.sleep(9999999999L); //kek
				}catch(Exception e){
				}
			}
		}
	}

	public void sendObject(WebSocket socket, Object object){
		requests.put(Pools.obtain(Request.class).set(socket, object));
		sendThread.interrupt();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake){
		Koru.log("Web client connection opened!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote){
		Koru.log("Web client connection closed! " + reason);
		server.disconnected(server.webmap.get(conn) == null ? null : server.webmap.get(conn));
	}

	@Override
	public void onMessage(WebSocket conn, String message){
		Object object = "deserialization doesn't exist anymore";
		if(!(object instanceof ChunkRequestPacket) && !(object instanceof PositionPacket))
			Koru.log(object.getClass());
		
		if(object instanceof ConnectPacket){
			Koru.log("Recieved connect packet.");
			ConnectPacket packet = (ConnectPacket)object;
			if( !server.webmap.containsKey(conn)){
				server.connectPacketRecieved(packet, conn, null);
			}else{
				//already connected, ignore?
			}
		}else if(server.webmap.containsKey(conn)){
			server.recieved(server.webmap.get(conn), object);
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex){
		ex.printStackTrace();
	}

}
