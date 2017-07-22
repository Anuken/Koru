package io.anuke.koru.network.packets;

import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.traits.DirectionTrait.Direction;

@Synced //<-- why is this here? nobody knows
public class PositionPacket{
	public float x, y, mouseangle;
	public Direction direction;
}
