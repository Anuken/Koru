package net.pixelstatic.koruserver;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.ConnectionComponent;
import net.pixelstatic.koru.components.InputComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.Network;
import net.pixelstatic.koru.network.IServer;
import net.pixelstatic.koru.network.Registrator;
import net.pixelstatic.koru.network.packets.*;
import net.pixelstatic.koru.systems.KoruEngine;
import net.pixelstatic.koru.world.InventoryTileData;
import net.pixelstatic.koru.world.World;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KoruServer extends IServer{
	//HashMap<Integer, Long> players = new HashMap<Integer, Long>();
	ObjectMap<Integer, ConnectionInfo> players = new ObjectMap<Integer, ConnectionInfo>();
	Server server;
	WebServer webserver;
	KoruUpdater updater;

	void start(){
		try{
			server = new Server(16384 * 256, 16384 * 256);
			Registrator.register(server.getKryo());
			server.addListener(new Listener.LagListener(Network.ping, Network.ping, new Listen(this)));
			server.start();
			server.bind(Network.port, Network.port);
			
			
			webserver = new WebServer(this, new InetSocketAddress(Network.port));
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
	
	public void recieved(ConnectionInfo info, Object object){
		if(object instanceof ConnectPacket){
			
			try{
				ConnectPacket connect = (ConnectPacket)object;

				KoruEntity player = new KoruEntity(EntityType.player);
				player.mapComponent(ConnectionComponent.class).connectionID = info.id;
				player.mapComponent(ConnectionComponent.class).name = connect.name;

				DataPacket data = new DataPacket();
				data.playerid = player.getID();

				ArrayList<Entity> entities = new ArrayList<Entity>();
				for(Entity entity : updater.engine.getEntities()){
					entities.add(entity);
				}

				data.entities = entities;
				
				
				
				sendTCP(info.id, data);

				sendToAllExceptTCP(info.id, player);
				

				player.addSelf();

				Koru.log("entity id: " + player.getID() + " connection id: " + player.mapComponent(ConnectionComponent.class).connectionID);
				Koru.log(connect.name + " has joined.");
			}catch(Exception e){
				e.printStackTrace();
				Koru.log("Critical error: failed sending player!");
				System.exit(1);
			}
		}else if(object instanceof PositionPacket){
			PositionPacket packet = (PositionPacket)object;
			if( !players.containsKey(info.id)) return;
			
			getPlayer(info).position().set(packet.x, packet.y);
			getPlayer(info).mapComponent(InputComponent.class).input.mouseangle = packet.mouseangle;
			
		}else if(object instanceof ChunkRequestPacket){
			ChunkRequestPacket packet = (ChunkRequestPacket)object;
			sendTCP(info.id, updater.world.createChunkPacket(packet));
		}else if(object instanceof InputPacket){
			InputPacket packet = (InputPacket)object;
			getPlayer(info).mapComponent(InputComponent.class).input.inputEvent(packet.type);
		}else if(object instanceof StoreItemPacket){
			StoreItemPacket packet = (StoreItemPacket)object;
			updater.world.tile(packet.x, packet.y).getBlockData(InventoryTileData.class).inventory.addItem(packet.stack);
			updater.world.updateTile(packet.x, packet.y);
		}else if(object instanceof BlockInputPacket){
			BlockInputPacket packet = (BlockInputPacket)object;
			updater.world.tile(packet.x, packet.y).block = packet.material;
			updater.world.updateTile(packet.x, packet.y);
		}
	}
	
	public void disconnected(ConnectionInfo info){
		try{
			if( !players.containsKey(info.id)){
				Koru.log("An unknown player has disconnected.");
				return;
			}
			Koru.log(getPlayer(info).mapComponent(ConnectionComponent.class).name + " has disconnected.");
			getPlayer(info).removeSelfServer();
			players.remove(info.id);
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Critical error: disconnect fail!");
		}
	}

	class Listen extends Listener{
		KoruServer koru;

		public Listen(KoruServer n){
			koru = n;
		}

		@Override
		public void disconnected(Connection connection){
			//TODO
			
		}

		@Override
		public void received(Connection connection, Object object){
			try{
				//TODO
			}catch(Exception e){
				e.printStackTrace();
				Koru.log("Packet error!");
				System.exit(1);
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

	public KoruEntity getPlayer(ConnectionInfo info){
		return updater.engine.getEntity(info.playerid);
	}
	
	public void send(ConnectionInfo info, Object object, boolean udp){
		if(info.isWeb()){
			info.socket.send(new byte[]{});
		}else{
			info.connection.sendTCP(object);
		}
	}
	
	//byte[] serialize(Object object){
	//	Output output = new Output();
	//	return server.getKryo().writeClass(output, type);
	//}
	
	public void sendToAllExceptTCP(int id, Object object){
		
	}
	
	public void sendToAllExceptUDP(int id, Object object){
		
	}

	@Override
	public void sendTCP(int id, Object object){
		
	}

	@Override
	public void sendUDP(int id, Object object){
		
	}

	@Override
	public long getFrameID(){
		return updater.frameid;
	}

	@Override
	public KoruEngine getEngine(){
		return updater.engine;
	}

	@Override
	public World getWorld(){
		return updater.world;
	}
	

	public static void main(String[] args){
		new KoruServer().start();
	}
}
