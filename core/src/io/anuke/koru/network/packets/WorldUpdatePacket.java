package io.anuke.koru.network.packets;

import io.anuke.koru.network.SyncBuffer;

import java.util.HashMap;

public class WorldUpdatePacket{
	public HashMap<Long, SyncBuffer> updates = new HashMap<Long, SyncBuffer>();
}
