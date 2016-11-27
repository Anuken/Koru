package io.anuke.koru.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.components.SyncComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.SyncBuffer;
import io.anuke.koru.network.SyncBuffer.PlayerSyncBuffer;
import io.anuke.koru.network.SyncBuffer.PositionSyncBuffer;
import io.anuke.koru.network.packets.WorldUpdatePacket;

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

		if(packet.updates.size != 0) IServer.instance().sendTCP(player.mapComponent(ConnectionComponent.class).connectionID, packet);
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
		},
		player{
			public SyncBuffer write(KoruEntity entity){
				return new PlayerSyncBuffer(entity.getID(), entity.getX(), entity.getY(), entity.mapComponent(RenderComponent.class).direction);
			}

			public void read(SyncBuffer buffer, KoruEntity entity){
				PlayerSyncBuffer position = (PlayerSyncBuffer)buffer;
				entity.mapComponent(RenderComponent.class).direction = position.direction;
				
				if(Vector2.dst(entity.getX(), entity.getY(), position.x, position.y) > 0.1f){
					entity.mapComponent(RenderComponent.class).walkframe += Gdx.graphics.getDeltaTime()*60f*3f;
				}else{
					entity.mapComponent(RenderComponent.class).walkframe = 0;
				}
				
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
