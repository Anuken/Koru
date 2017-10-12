package io.anuke.koru.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.IntIntMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.Prototypes;
import io.anuke.koru.entities.types.Effect;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.impl.Items;
import io.anuke.koru.modules.Network;
import io.anuke.koru.network.Net;
import io.anuke.koru.network.Net.Mode;
import io.anuke.koru.network.Net.NetProvider;
import io.anuke.koru.network.Registrator;
import io.anuke.koru.network.packets.*;
import io.anuke.koru.server.world.MapPreview;
import io.anuke.koru.systems.SyncSystem;
import io.anuke.koru.traits.*;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.util.ColorCodes;
import io.anuke.ucore.util.Mathf;

public class KoruServer implements NetProvider{
	boolean displayMap = true;
	//maps connection IDs to entity IDs
	IntIntMap connections = new IntIntMap();

	Server server;
	KoruUpdater updater;
	CommandHandler commands;
	
	//TODO thread safety? -- last connection used
	private Connection connection;

	private void setup(){
		Net.setProvider(true, this);
		registerHandlers();
		
		Resources.loadData();

		Effects.setEffectProvider((name, color, x, y) -> {
			Spark spark = Effect.create(name, color, x, y);
			sendSpark(spark);
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
		
		if(displayMap)
			createMapGraphics();
	}

	private void createMapGraphics(){
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.disableAudio(true);
		config.setTitle("Map Preview");
		config.setMaximized(true);

		new Lwjgl3Application((new MapPreview()), config);
	}
	
	private void registerHandlers(){
		
		Net.handle(PositionPacket.class, packet->{
			Spark player = getPlayer(connection.getID());
			
			player.pos().set(packet.x, packet.y);
			player.get(DirectionTrait.class).direction = packet.direction;
			player.get(InputTrait.class).input.mouseangle = packet.mouseangle;
		});
		
		Net.handle(SparkRequestPacket.class, packet->{
			Spark spark = updater.basis.getSpark(packet.id);
			if(spark != null){
				sendTo(connection.getID(), spark, Mode.TCP);
			}
		});
		
		Net.handle(ChatPacket.class, packet->{
			packet.sender = getPlayer(connection.getID()).get(ConnectionTrait.class).name;
			send(packet, Mode.TCP);
		});
		
		Net.handle(ChunkRequestPacket.class, packet->{
			sendTo(connection.getID(), updater.world.createChunkPacket(packet), Mode.UDP);
		});
		
		Net.handle(InputPacket.class, packet->{
			getPlayer(connection.getID()).get(InputTrait.class).input.inputEvent(packet.type, packet.data);
		});
		
		Net.handle(SlotChangePacket.class, packet->{
			Spark player = getPlayer(connection.getID());
			
			InventoryTrait inv = player.get(InventoryTrait.class);
			packet.slot = Mathf.clamp(packet.slot, 0, 3);
			inv.hotbar = packet.slot;
			packet.id = player.getID();
			packet.stack = inv.inventory[inv.hotbar];
			
			sendExcept(connection.getID(), packet, Mode.TCP);
		});
		
		Net.handle(RecipeSelectPacket.class, packet->{
			InventoryTrait inv = getPlayer(connection.getID()).get(InventoryTrait.class);
			inv.recipe = packet.recipe;
		});
		
		Net.handle(InventoryClickPacket.class, packet->{
			Spark player = getPlayer(connection.getID());
			
			InventoryTrait inv = player.get(InventoryTrait.class);
			inv.clickSlot(packet.index);
			inv.sendUpdate(updater.basis.getSpark(player.getID()));
		});
	}

	public void connectPacketRecieved(ConnectPacket packet, Connection connection){
		
		try{
			Koru.log("Connect packet recieved...");
			Spark player = new Spark(Prototypes.player);

			player.get(ConnectionTrait.class).connectionID = connection.getID();
			player.get(ConnectionTrait.class).name = packet.name;

			DataPacket data = new DataPacket();
			data.playerid = player.getID();
			data.time = Koru.world.time;

			ArrayList<Spark> sparks = new ArrayList<Spark>();

			updater.mapper.getNearbyEntities(player.pos().x, player.pos().y, SyncSystem.syncrange, spark -> {
				sparks.add(spark);
			});

			data.sparks = sparks;

			sendTo(connection.getID(), data, Mode.TCP);

			sendExcept(connection.getID(), player, Mode.TCP);

			player.add();

			InventoryTrait inv = player.get(InventoryTrait.class);

			inv.addItem(new ItemStack(Items.axe));
			inv.addItem(new ItemStack(Items.hammer));
			inv.addItem(new ItemStack(Items.stick, 10));
			inv.addItem(new ItemStack(Items.stone, 10));
			inv.sendUpdate(player);
			
			connections.put(connection.getID(), player.getID());

			sendChatMessage("[GREEN]" + packet.name + " [CHARTREUSE]has connected.");
			Koru.log("spark ID is " + player.getID() + ", connection ID is " + player.get(ConnectionTrait.class).connectionID);
			Koru.log(packet.name + " has joined.");
		}catch(Exception e){
			e.printStackTrace();
			Koru.log("Critical error: failed sending player!");
			System.exit(1);
		}
	}
	
	public void disconnected(int id){
		try{
			if(!connections.containsKey(id)){
				Koru.log("An unknown player has disconnected.");
				return;
			}
			
			Spark player = getPlayer(id);
			
			sendChatMessage("[GREEN]" + player.get(ConnectionTrait.class).name + " [CORAL]has disconnected.");
			Koru.log(player.get(ConnectionTrait.class).name + " has disconnected.");
			removeSpark(player);
			connections.remove(id, -1);
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
			KoruServer.this.disconnected(con.getID());
		}

		@Override
		public void received(Connection con, Object object){
			
			if(object instanceof ConnectPacket){
				Koru.log("recieved a connect packet from " + con.getID());
				ConnectPacket packet = (ConnectPacket) object;
				if(!connections.containsKey(con.getID())){
					connectPacketRecieved(packet, con);
				}
			}else if(connections.containsKey(con.getID())){
				KoruServer.this.connection = con;
				Net.onRecieve(object);
			}

		}
	}

	public void sendChatMessage(String message){
		ChatPacket packet = new ChatPacket();
		packet.message = message;
		send(packet, Mode.TCP);
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
		sendRange(spark, spark.pos().x, spark.pos().y, SyncSystem.syncrange, Mode.TCP);
	}

	@Override
	public void sendRange(Object object, float x, float y, float range, Mode mode){
		updater.mapper.getNearbyConnections(x, y, range, (spark) -> {
			sendTo(spark.get(ConnectionTrait.class).connectionID, object, mode);
		});
	}

	@Override
	public void send(Object object, Mode mode){
		if(mode == Mode.TCP){
			server.sendToAllTCP(object);
		}else{
			server.sendToAllUDP(object);
		}
	}

	@Override
	public void sendExcept(int id, Object object, Mode mode){
		if(mode == Mode.TCP){
			server.sendToAllExceptTCP(id, object);
		}else{
			server.sendToAllExceptUDP(id, object);
		}
	}

	@Override
	public void sendTo(int id, Object object, Mode mode){
		if(mode == Mode.TCP){
			server.sendToTCP(id, object);
		}else{
			server.sendToUDP(id, object);
		}
	}

	public Spark getPlayer(int connectionID){
		return updater.basis.getSpark(connections.get(connectionID, -1));
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
}
