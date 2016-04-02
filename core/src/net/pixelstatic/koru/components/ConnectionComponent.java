package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;
import com.esotericsoftware.kryonet.Connection;

@Synced
public class ConnectionComponent implements Component{
	public boolean local; //whether this is the local player
	public transient Connection connection;
	public String name;
}
