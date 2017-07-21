package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.SyncTrait;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.WorldUpdatePacket;
import io.anuke.ucore.util.Timers;

public class SyncSystem extends KoruSystem{
	static public float syncrange = 150;
	
	public SyncSystem(){
		super(Family.all(PositionComponent.class, ConnectionComponent.class).get());
	}
	
	public void update(float delta){
		if(Timers.get("synctimer", Network.packetFrequency))
			super.update(delta);
	}

	@Override
	protected void processEntity(KoruEntity player, float delta){
		
		WorldUpdatePacket packet = new WorldUpdatePacket();

		getEngine().map().getNearbySyncables(player.getX(), player.getY(), syncrange, (entity)->{
			if(entity == player) return;
			packet.updates.put(entity.getID(), entity.get(SyncTrait.class).type.write(entity));
		});

		
		if(packet.updates.size != 0) IServer.instance().sendTCP(player.get(ConnectionComponent.class).connectionID, packet);
	}
	
}
