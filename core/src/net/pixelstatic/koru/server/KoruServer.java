package net.pixelstatic.koru.server;

import java.util.ArrayList;
import java.util.HashMap;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.ConnectionComponent;
import net.pixelstatic.koru.components.InputComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.Network;
import net.pixelstatic.koru.network.Registrator;
import net.pixelstatic.koru.network.packets.*;
import net.pixelstatic.koru.world.InventoryTileData;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KoruServer{
	public static boolean active = false;
	HashMap<Integer, Long> players = new HashMap<Integer, Long>();
	Server server;
	KoruUpdater updater;

	void start(){
		KoruEntity.setServer(this);
		try{
			server = new Server(16384 * 256, 16384 * 256);
			Registrator.register(server.getKryo());
			server.addListener(new Listener.LagListener(Network.ping, Network.ping, new Listen(this)));
			server.start();
			server.bind(Network.port, Network.port);
			Koru.log("Server up.");
		}catch(Exception e){
			e.printStackTrace();
		}
		createUpdater();
	}

	private void createUpdater(){
		updater = new KoruUpdater(this);

		Thread thread = (new Thread(new Runnable(){
			public void run(){
				updater.run();
			}
		}));

		thread.setDaemon(true);
		thread.run();
	}

	class Listen extends Listener{
		KoruServer koru;

		public Listen(KoruServer n){
			koru = n;
		}

		@Override
		public void disconnected(Connection connection){
			try{
				if( !players.containsKey(connection.getID())){
					Koru.log("An unknown player has disconnected.");
					return;
				}
				Koru.log(getPlayer(connection.getID()).mapComponent(ConnectionComponent.class).name + " has disconnected.");
				getPlayer(connection.getID()).removeSelfServer();
				players.remove(connection.getID());
			}catch(Exception e){
				e.printStackTrace();
				Koru.log("Critical error: disconnect fail!");
			}
		}

		@Override
		public void received(Connection connection, Object object){
			try{
				if(object instanceof ConnectPacket){
					try{
						ConnectPacket connect = (ConnectPacket)object;

						KoruEntity player = new KoruEntity(EntityType.player);
						player.mapComponent(ConnectionComponent.class).connection = connection;
						player.mapComponent(ConnectionComponent.class).name = connect.name;

						DataPacket data = new DataPacket();
						data.playerid = player.getID();

						ArrayList<Entity> entities = new ArrayList<Entity>();
						for(Entity entity : updater.engine.getEntities()){
							entities.add(entity);
						}

						data.entities = entities;
						connection.sendTCP(data);

						server.sendToAllExceptTCP(connection.getID(), player);

						player.addSelf();
						players.put(connection.getID(), player.getID());

						Koru.log("entity id: " + player.getID() + " connection id: " + player.mapComponent(ConnectionComponent.class).connection.getID());
						Koru.log(connect.name + " has joined.");
					}catch(Exception e){
						e.printStackTrace();
						Koru.log("Critical error: failed sending player!");
						System.exit(1);
					}
				}else if(object instanceof PositionPacket){
					PositionPacket packet = (PositionPacket)object;
					if( !players.containsKey(connection.getID())) return;
					getPlayer(connection.getID()).position().set(packet.x, packet.y);
					getPlayer(connection.getID()).mapComponent(InputComponent.class).input.mouseangle = packet.mouseangle;
				}else if(object instanceof ChunkRequestPacket){
					ChunkRequestPacket packet = (ChunkRequestPacket)object;
					connection.sendTCP(updater.world.createChunkPacket(packet));
				}else if(object instanceof InputPacket){
					InputPacket packet = (InputPacket)object;
					getPlayer(connection.getID()).mapComponent(InputComponent.class).input.inputEvent(packet.type);
				}else if(object instanceof StoreItemPacket){
					StoreItemPacket packet = (StoreItemPacket)object;
					updater.world.tile(packet.x, packet.y).getBlockData(InventoryTileData.class).inventory.addItem(packet.stack);
					updater.world.updateTile(packet.x, packet.y);
				}else if(object instanceof BlockInputPacket){
					BlockInputPacket packet = (BlockInputPacket)object;
					updater.world.tile(packet.x, packet.y).block = packet.material;
					updater.world.updateTile(packet.x, packet.y);
				}
			}catch(Exception e){
				e.printStackTrace();
				Koru.log("Packet error!");
			}
		}
	}

	public void removeEntity(KoruEntity entity){
		EntityRemovePacket remove = new EntityRemovePacket();
		remove.id = entity.getID();
		server.sendToAllTCP(remove);
		updater.engine.removeEntity(entity);
	}

	public void sendEntity(KoruEntity entity){
		server.sendToAllTCP(entity);
	}

	public void sendToAll(Object object){
		server.sendToAllTCP(object);
	}

	public KoruEntity getPlayer(int cid){
		return updater.engine.getEntity(players.get(cid));
	}

	public static void main(String[] args){
		active = true;
		new KoruServer().start();
	}
}
