package net.pixelstatic.koruserver;

import java.net.InetSocketAddress;

import net.pixelstatic.koru.network.packets.ConnectPacket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Pool.Poolable;

public class WebServer extends WebSocketServer implements Runnable{
	final KoruServer server;
	final Thread sendThread = new Thread(this, "WebServer Thread");
	//CopyOnWriteArrayList<Request> requests = new CopyOnWriteArrayList<Request>();
	AtomicQueue<Request> requests = new AtomicQueue<Request>(1000);
	Json json = new Json();

	public WebServer(KoruServer server, InetSocketAddress address){
		super(address);
		this.server = server;
	}
	
	class Request implements Poolable{
		WebSocket socket;
		Object object;
		
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
	
	public void run(){
		while(true){
			Request request = null;
			while((request = requests.poll()) != null){
				request.socket.send(json.toJson(request.object));
			}
			try{
				Thread.sleep(9999999999L);
			}catch (Exception e){
				System.out.println("Interrupted!");
			}
		}
	}
	
	public void sendObject(WebSocket socket, Object object){
		requests.put(Pools.obtain(Request.class).set(socket, object));
		sendThread.interrupt();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake){
		
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote){
		server.disconnected(server.webmap.get(conn) == null ? null : server.webmap.get(conn));
	}

	@Override
	public void onMessage(WebSocket conn, String message){
		
		Object object = message.getBytes();//TODO make this deserialize
		
		if(object instanceof ConnectPacket){
			ConnectPacket packet = (ConnectPacket)object;
			if(!server.webmap.containsKey(conn)){
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
		
	}

}
