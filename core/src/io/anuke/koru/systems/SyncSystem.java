package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.InputComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.components.SyncComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.SyncData;
import io.anuke.koru.network.SyncData.PlayerSyncData;
import io.anuke.koru.network.SyncData.PositionSyncData;
import io.anuke.koru.network.packets.WorldUpdatePacket;

public class SyncSystem extends KoruSystem{
	static public float syncrange = 150;
	
	public SyncSystem(){
		super(Family.all(PositionComponent.class, ConnectionComponent.class).get());
	}

	@Override
	protected void processEntity(KoruEntity player, float delta){
		if(IServer.instance().getFrameID() % Network.packetFrequency != 0) return;

		WorldUpdatePacket packet = new WorldUpdatePacket();
		
		getEngine().map().getNearbySyncables(player.getX(), player.getY(), syncrange, (entity)->{
			if(entity == player) return;
			packet.updates.put(entity.getID(), entity.mapComponent(SyncComponent.class).type.write(entity));
		});

		
		if(packet.updates.size != 0) IServer.instance().sendTCP(player.mapComponent(ConnectionComponent.class).connectionID, packet);
	}

	public enum SyncType{
		position{
			public SyncData write(KoruEntity entity){
				return new PositionSyncData(entity.getID(), entity.getX(), entity.getY());
			}

			public void read(SyncData buffer, KoruEntity entity){
				PositionSyncData position = (PositionSyncData)buffer;
				SyncComponent sync = entity.mapComponent(SyncComponent.class);
				if(sync.interpolator != null){
					sync.interpolator.push(entity, position.x, position.y);
				}else{
					entity.position().set(position.x, position.y);
				}
			}
		},
		player{
			public SyncData write(KoruEntity entity){
				return new PlayerSyncData(entity.getID(), entity.getX(), entity.getY(), entity.get(InputComponent.class).input.mouseangle, entity.mapComponent(RenderComponent.class).direction);
			}

			public void read(SyncData buffer, KoruEntity entity){
				PlayerSyncData position = (PlayerSyncData)buffer;
				entity.mapComponent(RenderComponent.class).direction = position.direction;
				
				if(Vector2.dst(entity.getX(), entity.getY(), position.x, position.y) > 0.1f){
					entity.mapComponent(RenderComponent.class).walkframe += Gdx.graphics.getDeltaTime()*60f*3f;
				}else{
					entity.mapComponent(RenderComponent.class).walkframe = 0;
				}
				
				entity.get(InputComponent.class).input.mouseangle = position.mouse;
				
				SyncComponent sync = entity.mapComponent(SyncComponent.class);
				
				if(sync.interpolator != null){
					sync.interpolator.push(entity, position.x, position.y);
				}else{
					entity.position().set(position.x, position.y);
				}
			}
		};

		public abstract SyncData write(KoruEntity entity);

		public abstract void read(SyncData buffer, KoruEntity entity);

	}
}
