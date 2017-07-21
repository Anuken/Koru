package io.anuke.koru.network.packets;

import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.network.syncing.SyncData;

public class WorldUpdatePacket{
	public ObjectMap<Integer, SyncData> updates = new ObjectMap<>();
}
