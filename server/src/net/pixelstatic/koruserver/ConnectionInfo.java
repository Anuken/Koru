package net.pixelstatic.koruserver;

import org.java_websocket.WebSocket;

import com.esotericsoftware.kryonet.Connection;

public class ConnectionInfo{
	public final int id;
	public final long playerid;
	public final Connection connection;
	public final WebSocket socket;
	
	public ConnectionInfo(long playerid, Connection connection, int id){
		this.id = id;
		this.playerid = playerid;
		this.connection = connection;
		this.socket = null;
	}
	
	public ConnectionInfo(long playerid, WebSocket socket, int id){
		this.id = id;
		this.playerid = playerid;
		this.connection =null;
		this.socket = socket;
	}
	
	public boolean isWeb(){
		return connection == null;
	}
}
