package net.pixelstatic.koru.network.packets;

import java.util.HashMap;

import net.pixelstatic.koru.network.SyncBuffer;

public class WorldUpdatePacket{
	public HashMap<Long, SyncBuffer> updates = new HashMap<Long, SyncBuffer>();
}
