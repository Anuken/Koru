package io.anuke.koru.network.packets;

import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.network.SyncBuffer;

public class WorldUpdatePacket{
	public ObjectMap<Long, SyncBuffer> updates = new ObjectMap<Long, SyncBuffer>();
}
