package io.anuke.koru.server;

import com.esotericsoftware.kryonet.Connection;

public class ConnectionInfo{
	private static int lastid;
	
	public final int id;
	public final int playerid;
	public final Connection connection;
	
	public ConnectionInfo(int playerid, Connection connection){
		this.id = lastid++;
		this.playerid = playerid;
		this.connection = connection;
	}
}
