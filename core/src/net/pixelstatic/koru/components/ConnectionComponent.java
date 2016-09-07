package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;

@Synced
public class ConnectionComponent implements Component{
	public boolean local; //whether this is the local player
	public transient int connectionID;
	public String name;
}
