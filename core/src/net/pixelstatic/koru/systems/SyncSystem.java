package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.components.ConnectionComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.components.SyncComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.Network;
import net.pixelstatic.koru.network.IServer;
import net.pixelstatic.koru.network.SyncBuffer;
import net.pixelstatic.koru.network.SyncBuffer.PositionSyncBuffer;
import net.pixelstatic.koru.network.packets.WorldUpdatePacket;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

public class SyncSystem extends IteratingSystem{
	Family family = Family.all(SyncComponent.class).get();

	public SyncSystem(){
		super(Family.all(PositionComponent.class, ConnectionComponent.class).get());
	}

	@Override
	protected void processEntity(Entity aentity, float deltaTime){
		if(IServer.instance().getFrameID() % Network.packetFrequency != 0) return;
		KoruEntity player = (KoruEntity)aentity;
		ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);

		WorldUpdatePacket packet = new WorldUpdatePacket();
		for(Entity entity : entities){
			if(entity == player) continue;
			KoruEntity ko = (KoruEntity)entity;
			packet.updates.put(ko.getID(), ko.mapComponent(SyncComponent.class).type.write(ko));
		}
		if(packet.updates.size() != 0) IServer.instance().sendTCP(player.mapComponent(ConnectionComponent.class).connectionID, packet);
	}

	public enum SyncType{
		position{
			public SyncBuffer write(KoruEntity entity){
				return new PositionSyncBuffer(entity.getID(), entity.getX(), entity.getY());
			}

			public void read(SyncBuffer buffer, KoruEntity entity){
				PositionSyncBuffer position = (PositionSyncBuffer)buffer;
				SyncComponent sync = entity.mapComponent(SyncComponent.class);
				if(sync.interpolator != null){
					sync.interpolator.push(entity, position.x, position.y);
				}else{
					entity.position().set(position.x, position.y);
				}
			}
		};

		public abstract SyncBuffer write(KoruEntity entity);

		public abstract void read(SyncBuffer buffer, KoruEntity entity);

	}
}
