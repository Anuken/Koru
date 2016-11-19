package io.anuke.koru;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.java_websocket.WebSocket;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.InputComponent;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.generation.GeneratedMaterial;
import io.anuke.koru.generation.MaterialManager;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.Registrator;
import io.anuke.koru.network.packets.BlockInputPacket;
import io.anuke.koru.network.packets.ChatPacket;
import io.anuke.koru.network.packets.ChunkRequestPacket;
import io.anuke.koru.network.packets.ConnectPacket;
import io.anuke.koru.network.packets.DataPacket;
import io.anuke.koru.network.packets.EntityRemovePacket;
import io.anuke.koru.network.packets.InputPacket;
import io.anuke.koru.network.packets.InventoryClickPacket;
import io.anuke.koru.network.packets.InventoryUpdatePacket;
import io.anuke.koru.network.packets.MaterialRequestPacket;
import io.anuke.koru.network.packets.PositionPacket;
import io.anuke.koru.network.packets.StoreItemPacket;
import io.anuke.koru.systems.KoruEngine;
import io.anuke.koru.utils.Text;
import io.anuke.koru.world.InventoryTileData;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.World;

public class KoruServer extends IServer{
	ObjectMap<Integer, ConnectionInfo> connections = new ObjectMap<Integer, ConnectionInfo>();
	ObjectMap<Connection, ConnectionInfo> kryomap = new ObjectMap<Connection, ConnectionInfo>();
	ObjectMap<WebSocket, ConnectionInfo> webmap = new ObjectMap<WebSocket, ConnectionInfo>();

	Server server;
	WebServer webserver;
	KoruUpdater updater;
	GraphicsHandler graphics;

	void setup(){
		try{
			server = new Server(16384 * 256, 16384 * 256);
			Registrator.register(server.getKryo());
			server.addListener(new Listener.LagListener(Network.ping, Network.ping, new Listen(this)));
			server.start();
			server.bind(Network.port, Network.port);

		//	webserver = new WebServer(this, new InetSocketAddress(Network.port+1));
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
		thread.start();
		
		createMapGraphics();
	}
	
	void createGraphics(){
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.disableAudio(true);
		config.setInitialVisible(false);
		
		new Lwjgl3Application((graphics=new GraphicsHandler()), config);
	}
	
	void createMapGraphics(){
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.disableAudio(true);
		config.setTitle("Map Preview");
		
		new Lwjgl3Application((new MapPreview()), config);
	}

	public void connectPacketRecieved(ConnectPacket packet, WebSocket socket, Connection connection){
		try{

			KoruEntity player = new KoruEntity(EntityType.player);

			ConnectionInfo info = socket == null ? new ConnectionInfo(player.getID(), connection) : new ConnectionInfo(player.getID(), socket);

			registerConnection(info);

			player.mapComponent(ConnectionComponent.class).connectionID = info.id;
			player.mapComponent(ConnectionComponent.class).name = packet.name;

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
			
			sendChatMessage("[GREEN]" + packet.name + " [CHARTREUSE]has connected.");
			Koru.log("entity id: " + player.getID() + " connection id: " + player.mapComponent(ConnectionComponent.class).connectionID);
			Koru.log(packet.name + " has joined.");
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Critical error: failed sending player!");
			System.exit(1);
		}
	}

	public void recieved(ConnectionInfo info, Object object){
		try{
			if(object instanceof PositionPacket){
				PositionPacket packet = (PositionPacket)object;
				if( !connections.containsKey(info.id)) return;

				getPlayer(info).position().set(packet.x, packet.y);
				getPlayer(info).mapComponent(InputComponent.class).input.mouseangle = packet.mouseangle;
			}else if(object instanceof ChatPacket){
				ChatPacket packet = (ChatPacket)object;
				packet.sender = updater.engine.getEntity(info.playerid).getComponent(ConnectionComponent.class).name;
				sendToAll(packet);
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
				updater.world.tile(packet.x, packet.y).setMaterial(packet.material);
				updater.world.updateTile(packet.x, packet.y);
			}else if(object instanceof InventoryClickPacket){
				InventoryClickPacket packet = (InventoryClickPacket)object;
				InventoryComponent inv = getPlayer(info).getComponent(InventoryComponent.class);
				inv.clickSlot(packet.x, packet.y);
				
				Koru.log("recieved inventory click packet");
				InventoryUpdatePacket update = new InventoryUpdatePacket();
				update.selected = inv.selected;
				update.stacks = inv.inventory;
				send(info, update, false);
			}else if(object instanceof MaterialRequestPacket){
				MaterialRequestPacket packet = (MaterialRequestPacket)object;
				Material mat = MaterialManager.instance().getMaterial(packet.id);
				if(mat == null || !(mat instanceof GeneratedMaterial)){
					Koru.log("Invalid material requested: " + mat.id());
					return;
				}
				Koru.log("Sending material type to player: " + packet.id);
				graphics.sendMaterial(info.id, (GeneratedMaterial)mat);
			}
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Packet error!");
			System.exit(1);
		}
	}

	public void disconnected(ConnectionInfo info){
		try{
			if(info == null){
				Koru.log("An unknown player has disconnected.");
				return;
			}
			sendChatMessage("[GREEN]" + getPlayer(info).mapComponent(ConnectionComponent.class).name + " [CORAL]has disconnected.");
			Koru.log(getPlayer(info).mapComponent(ConnectionComponent.class).name + " has disconnected.");
			getPlayer(info).removeSelfServer();
			removeConnection(info);
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
		public void disconnected(Connection con){
			KoruServer.this.disconnected(kryomap.get(con) == null ? null : kryomap.get(con));
		}

		@Override
		public void received(Connection con, Object object){
			if(object instanceof ConnectPacket){
				ConnectPacket packet = (ConnectPacket)object;
				if( !kryomap.containsKey(con)){
					connectPacketRecieved(packet, null, con);
				}else{
					//already connected, ignore?
				}
			}else if(kryomap.containsKey(con)){
				recieved(kryomap.get(con), object);
			}

		}
	}
	
	public void sendChatMessage(String message){
		ChatPacket packet = new ChatPacket();
		packet.message = message;
		sendToAll(packet);
	}	

	public void registerConnection(ConnectionInfo info){
		connections.put(info.id, info);
		if(info.isWeb()){
			webmap.put(info.socket, info);
		}else{
			kryomap.put(info.connection, info);
		}
	}

	public void removeConnection(ConnectionInfo info){
		connections.remove(info.id);
		if(info.isWeb()){
			webmap.remove(info.socket);
		}else{
			kryomap.remove(info.connection);
		}
	}

	public void removeEntity(KoruEntity entity){
		EntityRemovePacket remove = new EntityRemovePacket();
		remove.id = entity.getID();
		server.sendToAllTCP(remove);
		updater.engine.removeEntity(entity);
	}

	public void sendEntity(KoruEntity entity){
		sendToAll(entity);
	}

	public void sendToAll(Object object){
		for(ConnectionInfo info : connections.values()){
			send(info, object, false);
		}
	}

	public KoruEntity getPlayer(ConnectionInfo info){
		return updater.engine.getEntity(info.playerid);
	}

	public void send(ConnectionInfo info, Object object, boolean udp){
		if(info.isWeb()){
			//webserver.sendObject(info.socket, object); //screw GWT!
		}else{
			if(udp){
				info.connection.sendUDP(object);
			}else{
				/*int size = */info.connection.sendTCP(object);
				
				//Koru.log("Sending " + object.getClass().getSimpleName() + ": " +size  + "b");
			}
		}
	}
	
	public void sendToAllExceptTCP(int id, Object object){
		for(ConnectionInfo info : connections.values()){
			if(info.id != id)
			send(info, object, false);
		}
	}

	public void sendToAllExceptUDP(int id, Object object){
		for(ConnectionInfo info : connections.values()){
			if(info.id != id)
			send(info, object, true);
		}
	}

	@Override
	public void sendTCP(int id, Object object){
		send(connections.get(id), object, false);
	}

	@Override
	public void sendUDP(int id, Object object){
		send(connections.get(id), object, true);
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
		System.out.println(Text.FLUSH);
		System.out.flush();
		if(args.length > 0 && args[0].toLowerCase().equals("-clearworld")){
			Koru.log("Clearing world.");
			try {
				Files.list(Paths.get("world")).forEach((Path path)->{
					try {
						Files.delete(path);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		new KoruServer().setup();
	}
	
	public static KoruServer instance(){
		return (KoruServer)IServer.instance();
	}
}
