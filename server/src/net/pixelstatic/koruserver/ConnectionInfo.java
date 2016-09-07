package net.pixelstatic.koruserver;

import org.java_websocket.WebSocket;

import com.esotericsoftware.kryonet.Connection;

public class ConnectionInfo{
	private static int lastid;
	public final int id;
	public final long playerid;
	public final Connection connection;
	public final WebSocket socket;
	
	public ConnectionInfo(long playerid, Connection connection){
		this.id = lastid++;
		this.playerid = playerid;
		this.connection = connection;
		this.socket = null;
	}
	
	public ConnectionInfo(long playerid, WebSocket socket){
		this.id = lastid++;
		this.playerid = playerid;
		this.connection =null;
		this.socket = socket;
	}
	
	public boolean isWeb(){
		return connection == null;
	}
}
