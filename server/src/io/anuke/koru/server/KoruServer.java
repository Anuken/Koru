package io.anuke.koru.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.Prototypes;
import io.anuke.koru.entities.types.Effect;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.Items;
import io.anuke.koru.modules.Network;
import io.anuke.koru.modules.World;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.Registrator;
import io.anuke.koru.network.packets.*;
import io.anuke.koru.server.world.MapPreview;
import io.anuke.koru.systems.SyncSystem;
import io.anuke.koru.traits.*;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.Basis;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.util.ColorCodes;
import io.anuke.ucore.util.Mathf;

public class KoruServer extends IServer{
	ObjectMap<Integer, ConnectionInfo> connections = new ObjectMap<Integer, ConnectionInfo>();
	ObjectMap<Connection, ConnectionInfo> kryomap = new ObjectMap<Connection, ConnectionInfo>();

	Server server;
	KoruUpdater updater;
	GraphicsHandler graphics;
	CommandHandler commands;

	void setup(){
		Resources.loadMaterials();
		
		Effects.setEffectProvider((name, color, x, y)->{
			Spark spark = Effect.create(name, color, x, y);
			addSpark(spark);
		});
		
		commands = new CommandHandler(this);

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

		Thread thread = (new Thread(() -> {
			updater.run();
		}));

		thread.setDaemon(true);
		thread.start();

		//createGraphics();

		//createMapGraphics();
	}

	void createGraphics(){
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.disableAudio(true);
		config.setInitialVisible(false);

		new Lwjgl3Application((graphics = new GraphicsHandler()), config);
	}

	void createMapGraphics(){
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.disableAudio(true);
		config.setTitle("Map Preview");
		config.setMaximized(true);

		new Lwjgl3Application((new MapPreview()), config);
	}

	public void connectPacketRecieved(ConnectPacket packet, Connection connection){
		try{
			Koru.log("Connect packet recieved...");
			Spark player = new Spark(Prototypes.player);
			ConnectionInfo info = new ConnectionInfo(player.getID(), connection);
			
			registerConnection(info);

			player.get(ConnectionTrait.class).connectionID = info.id;
			player.get(ConnectionTrait.class).name = packet.name;

			DataPacket data = new DataPacket();
			data.playerid = player.getID();
			data.time = getWorld().time;

			ArrayList<Spark> sparks = new ArrayList<Spark>();

			updater.mapper.getNearbyEntities(player.pos().x, player.pos().y, SyncSystem.syncrange, spark -> {
				sparks.add(spark);
			});

			data.sparks = sparks;

			send(info.id, data, false);

			sendToAllExceptTCP(info.id, player);

			player.add();
			
			//NOTE: IF THE FOLLOWING MESSAGES DO NOT SEND, YOU HAVE A CLASSPATH ERROR!
			//UPDATE the UCORE VERSION
			
			/*
			InventoryComponent inv = player.get(InventoryComponent.class);
			
			inv.inventory[3][0] = new ItemStack(Items.woodhammer);
			inv.inventory[2][0] = new ItemStack(Items.woodaxe);
			inv.inventory[1][0] = new ItemStack(Items.woodpickaxe);
			inv.inventory[0][0] = new ItemStack(Items.woodsword);
			inv.sendUpdate(player);
			 */
			
			InventoryTrait inv = player.get(InventoryTrait.class);
			inv.addItem(new ItemStack(Items.stick, 10));
			inv.addItem(new ItemStack(Items.stone, 10));
			inv.sendUpdate(player);
			
			//doesn't seem to work
			//player.get(InventoryComponent.class).sendHotbarUpdate(player);

			//for(ConnectionInfo i : connections.values())
			//	player.get(InventoryComponent.class).sendHotbarUpdate(updater.basis.getSpark(i.playerid), info.id);

			sendChatMessage("[GREEN]" + packet.name + " [CHARTREUSE]has connected.");
			Koru.log("spark ID is " + player.getID() + ", connection ID is " + player.get(ConnectionTrait.class).connectionID);
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
				PositionPacket packet = (PositionPacket) object;
				if(!connections.containsKey(info.id) || getPlayer(info) == null)
					return;
				
				getPlayer(info).pos().set(packet.x, packet.y);
				getPlayer(info).get(DirectionTrait.class).direction = packet.direction;
				getPlayer(info).get(InputTrait.class).input.mouseangle = packet.mouseangle;
			}else if(object instanceof SparkRequestPacket){
				SparkRequestPacket packet = (SparkRequestPacket) object;
				Spark spark = updater.basis.getSpark(packet.id);
				if(spark != null){
					send(info, spark, false);
				}
			}else if(object instanceof ChatPacket){
				ChatPacket packet = (ChatPacket) object;
				packet.sender = updater.basis.getSpark(info.playerid).get(ConnectionTrait.class).name;
				sendToAll(packet);
			}else if(object instanceof ChunkRequestPacket){
				ChunkRequestPacket packet = (ChunkRequestPacket) object;
				send(info.id, updater.world.createChunkPacket(packet), false);
			}else if(object instanceof InputPacket){
				InputPacket packet = (InputPacket) object;
				getPlayer(info).get(InputTrait.class).input.inputEvent(packet.type, packet.data);
			}else if(object instanceof SlotChangePacket){
				SlotChangePacket packet = (SlotChangePacket) object;
				InventoryTrait inv = getPlayer(info).get(InventoryTrait.class);
				packet.slot = Mathf.clamp(packet.slot, 0, 3);
				inv.hotbar = packet.slot;
				packet.id = info.playerid;
				packet.stack = inv.inventory[inv.hotbar];
				sendToAllExceptTCP(info.id, packet);
			}else if(object instanceof RecipeSelectPacket){
				RecipeSelectPacket packet = (RecipeSelectPacket) object;
				InventoryTrait inv = getPlayer(info).get(InventoryTrait.class);
				inv.recipe = packet.recipe;
			}else if(object instanceof BlockInputPacket){
				BlockInputPacket packet = (BlockInputPacket) object;
				Material mat = Material.getMaterial(packet.material);
				Tile tile = updater.world.getTile(packet.x, packet.y);
				
				if(mat.getType().tile()){
					tile.addTile(mat);
				}else{
					tile.setBlockMaterial(mat);
				}
				updater.world.updateTile(packet.x, packet.y);

			}else if(object instanceof InventoryClickPacket){
				InventoryClickPacket packet = (InventoryClickPacket) object;
				InventoryTrait inv = getPlayer(info).get(InventoryTrait.class);
				inv.clickSlot(packet.index);

				inv.sendUpdate(updater.basis.getSpark(info.playerid));
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
			sendChatMessage("[GREEN]" + getPlayer(info).get(ConnectionTrait.class).name + " [CORAL]has disconnected.");
			Koru.log(getPlayer(info).get(ConnectionTrait.class).name + " has disconnected.");
			removeSpark(getPlayer(info));
			removeConnection(info);
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Critical error: disconnect fail!");
		}
	}

	class Listen extends Listener{
		KoruServer koru;

		public Listen(KoruServer n) {
			koru = n;
		}

		@Override
		public void disconnected(Connection con){
			KoruServer.this.disconnected(kryomap.get(con) == null ? null : kryomap.get(con));
		}

		@Override
		public void received(Connection con, Object object){
			
			if(object instanceof ConnectPacket){
				Koru.log("recieved a connect packet from " + con.getID());
				ConnectPacket packet = (ConnectPacket) object;
				if(!kryomap.containsKey(con)){
					connectPacketRecieved(packet, con);
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
		kryomap.put(info.connection, info);
	}
	
	public void removeConnection(ConnectionInfo info){
		connections.remove(info.id);
		kryomap.remove(info.connection);
	}
	
	@Override
	public void removeSpark(Spark spark){
		SparkRemovePacket remove = new SparkRemovePacket();
		remove.id = spark.getID();
		server.sendToAllTCP(remove);
		updater.basis.removeSpark(spark);
		spark.remove();
	}
	
	@Override
	public void sendSpark(Spark spark){
		sendToAllIn(spark, spark.pos().x, spark.pos().y, SyncSystem.syncrange);
	}
	
	@Override
	public void sendToAllIn(Object object, float x, float y, float range){
		updater.mapper.getNearbyConnections(x, y, range, (spark) -> {
			send(spark.get(ConnectionTrait.class).connectionID, object, false);
		});
	}
	
	@Override
	public void sendToAll(Object object){
		for(ConnectionInfo info : connections.values()){
			send(info, object, false);
		}
	}
	
	@Override
	public void sendToAllExcept(int id, Object object){
		sendToAllExceptTCP(id, object);
	}
	
	@Override
	public void send(int id, Object object, boolean udp){
		send(connections.get(id), object, udp);
	}
	
	public void addSpark(Spark spark){
		sendSpark(spark.add());
	}
	
	public Spark getPlayer(ConnectionInfo info){
		return updater.basis.getSpark(info.playerid);
	}

	public void send(ConnectionInfo info, Object object, boolean udp){
		if(udp)
			info.connection.sendUDP(object);
		else
			info.connection.sendTCP(object);
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
	public Basis getBasis(){
		return updater.basis;
	}

	@Override
	public World getWorld(){
		return updater.world;
	}

	public static void main(String[] args){
		System.out.println(ColorCodes.FLUSH);
		System.out.flush();
		if(args.length > 0 && args[0].toLowerCase().equals("-clearworld")){
			Koru.log("Clearing world.");
			try{
				Files.list(Paths.get("world")).forEach((Path path) -> {
					try{
						Files.delete(path);
					}catch(IOException e){
						e.printStackTrace();
					}
				});
			}catch(IOException e){
				e.printStackTrace();
			}

		}
		new KoruServer().setup();
	}

	public static KoruServer instance(){
		return (KoruServer) IServer.instance();
	}
}
