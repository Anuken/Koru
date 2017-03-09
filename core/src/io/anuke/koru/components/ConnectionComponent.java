package io.anuke.koru.components;

import io.anuke.koru.network.syncing.SyncData.Synced;

@Synced
public class ConnectionComponent implements KoruComponent{
	public boolean local; //whether this is the local player
	public transient int connectionID;
	public String name;
}
