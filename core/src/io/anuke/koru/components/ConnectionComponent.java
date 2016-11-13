package io.anuke.koru.components;

import io.anuke.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;

@Synced
public class ConnectionComponent implements Component{
	public boolean local; //whether this is the local player
	public transient int connectionID;
	public String name;
}
