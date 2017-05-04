package io.anuke.koru.systems;

import com.badlogic.ashley.core.Family;

import io.anuke.koru.components.*;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.WorldUpdatePacket;
import io.anuke.koru.network.syncing.SyncData;
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
			packet.updates.put(entity.getID(), entity.get(SyncComponent.class).type.write(entity));
		});

		
		if(packet.updates.size != 0) IServer.instance().sendTCP(player.get(ConnectionComponent.class).connectionID, packet);
	}

	public enum SyncType{
		position{
			public SyncData write(KoruEntity entity){
				return new SyncData(entity, entity.getX(), entity.getY());
			}

			public void read(SyncData data, KoruEntity entity){
				SyncComponent sync = entity.get(SyncComponent.class);
				
				if(sync.interpolator != null){
					sync.interpolator.push(entity, data.x(), data.y());
				}else{
					entity.position().set(data.x(), data.y());
				}
			}
		},
		physics{
			public SyncData write(KoruEntity entity){
				return new SyncData(entity, entity.getX(), entity.getY(), entity.collider().collider.getVelocity());
			}

			public void read(SyncData data, KoruEntity entity){
				SyncComponent sync = entity.get(SyncComponent.class);
				
				entity.collider().collider.getVelocity().set(data.get(2));
				
				if(sync.interpolator != null){
					sync.interpolator.push(entity, data.x(), data.y());
				}else{
					entity.position().set(data.x(), data.y());
				}
			}
		},
		player{
			public SyncData write(KoruEntity entity){
				return new SyncData(entity, 
						entity.getX(), entity.getY(), 
						entity.get(InputComponent.class).input.mouseangle, 
						entity.renderer().direction,
						entity.collider().collider.getVelocity());
			}

			public void read(SyncData data, KoruEntity entity){
				entity.get(RenderComponent.class).direction = data.get(3);
				float x = data.get(0);
				float y = data.get(1);
				
				entity.collider().collider.getVelocity().set(data.get(4));
				
				entity.get(RenderComponent.class).renderer.walking = entity.collider().collider.getVelocity().len() > 0.01f;
				
				entity.get(InputComponent.class).input.mouseangle = data.get(2);
				SyncComponent sync = entity.get(SyncComponent.class);
				
				if(sync.interpolator != null){
					sync.interpolator.push(entity, x, y);
				}else{
					entity.position().set(x, y);
				}
			}
		};

		public abstract SyncData write(KoruEntity entity);

		public abstract void read(SyncData buffer, KoruEntity entity);

	}
	
}
