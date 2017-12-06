package io.anuke.koru.modules;

import static io.anuke.koru.Koru.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.IntSet.IntSetIterator;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEvents.Unload;
import io.anuke.koru.modules.Control.GameState;
import io.anuke.koru.network.Net;
import io.anuke.koru.network.Net.Mode;
import io.anuke.koru.network.Net.NetProvider;
import io.anuke.koru.network.Registrator;
import io.anuke.koru.network.packets.*;
import io.anuke.koru.traits.*;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Bits;

public class Network extends Module implements NetProvider{
	public static final int port = 7575;
	public static final int ping = 0;
	public static final int pingInterval = 60;
	public static final int packetFrequency = 3;
	public static final float sparkUnloadRange = 600;

	public String ip = "localhost";
	public boolean connecting;
	private Client client;

	private Spark player;

	private boolean connected;
	private boolean chunksAdded = false;
	private IntArray tempids = new IntArray();
	private Array<Spark> sparkQueue = new Array<>();
	private IntSet sparksToRemove = new IntSet();
	private IntSet requestedEntities = new IntSet();

	@Override
	public void init(){
		registerPackets();
		
		Net.setProvider(false, this);

		int buffer = (int) Math.pow(2, 6) * 8192;
		client = new Client(buffer, buffer);
		Registrator.register(client.getKryo());
		client.addListener(new Listen());
		client.start();

		player = Koru.control.player;
	}

	private void registerPackets(){

		Net.handle(DataPacket.class, data -> {

			Gdx.app.postRunnable(() -> {
				Koru.log("Recieving a data packet... ");

				//TODO thread safety?
				Koru.basis.clearSparks();
				Koru.log("Recieved " + data.sparks.size() + " sparks.");

				for(Spark spark : data.sparks){
					Koru.basis.addSpark(spark);
				}

				Koru.world.time = data.time;
				Koru.control.player.resetID(data.playerid);

				Koru.basis.addSpark(Koru.control.player);
				Koru.log("Recieved data packet.");
			});
		});

		Net.handle(WorldUpdatePacket.class, p -> {
			for(Integer key : p.updates.keys()){
				Spark spark = Koru.basis.getSpark(key);
				if(spark == null){
					requestEntity(key);
					continue;
				}
				spark.get(SyncTrait.class).type.read(p.updates.get(key), spark);
			}
		});

		Net.handle(ChunkPacket.class, p -> {
			world.loadChunks(p);
			chunksAdded = true;
		});

		Net.handle(TileUpdatePacket.class, p -> {
			if(world.inClientBounds(p.x, p.y)){
				world.setTile(p.x, p.y, p.tile);
			}

			chunksAdded = true;
		});

		Net.handle(MenuOpenPacket.class, p -> {
			Gdx.app.postRunnable(() -> {
				ui.openMenu(p.type);
			});
		});

		Net.handle(SlotChangePacket.class, p -> {
			if(Koru.basis.getSpark(p.id) == null)
				return;

			Koru.basis.getSpark(p.id).get(InventoryTrait.class).inventory[0] = p.stack;
		});

		Net.handle(InventoryUpdatePacket.class, p -> {
			player.get(InventoryTrait.class).set(p.stacks, p.selected);
		});

		Net.handle(ChatPacket.class, p -> {
			Gdx.app.postRunnable(() -> {
				ui.handleChatMessage(p.message, p.sender);
			});
		});
		
		Net.handle(BatchTileUpdatePacket.class, packet->{
			for(int i = 0; i < packet.coords.length; i ++){
				int x = (int)(packet.coords[i] >> 32);
				int y = (int)(packet.coords[i]);
				short floor = (short) Bits.getLeftShort(packet.ids[i]);
				short wall = (short) Bits.getRightShort(packet.ids[i]);
				
				Tile tile = Koru.world.getTile(x, y);
				tile.setWall(Material.getMaterial(wall));
				tile.addFloor(Material.getMaterial(floor));
				
				Koru.renderer.invalidateTiles();;
			}
		});

		Net.handle(Spark.class, spark -> {
			sparkQueue.add(spark);
		});

		Net.handle(SparkRemovePacket.class, p -> {
			sparksToRemove.add(p.id);
		});

	}

	public void connect(){
		try{
			connecting = true;
			client.connect(1200, ip, port, port);
			Koru.log("Connecting to server..");
		}catch(Exception e){
			connecting = false;
			connected = false;
			e.printStackTrace();
			Koru.ui.showError("[crimson]Failed to connect to server:", (e.getCause() == null ? e.getMessage() : e.getCause().getMessage()));
			Koru.log("Connection failed!");
		}

		connecting = false;
		//initialconnect = true;
	}

	void requestEntity(int id){
		if(!requestedEntities.contains(id) && !sparksToRemove.contains(id)){
			requestedEntities.add(id);

			SparkRequestPacket request = new SparkRequestPacket();
			request.id = id;
			client.sendTCP(request);
		}
	}

	private void disconnect(){
		connected = false;
		connecting = false;
		Koru.ui.showError("Connection error: Timed out.");

		//reset everything.

		for(int x = 0; x < world.chunks.length; x++){
			for(int y = 0; y < world.chunks[x].length; y++){
				world.chunks[x][y] = null;
			}
		}

		Koru.basis.clearSparks();
	}

	@Override
	public void update(){
		while(sparkQueue.size != 0){

			Spark spark = sparkQueue.pop();
			if(spark == null){
				continue;
			}

			requestedEntities.remove(spark.getID());

			if(sparksToRemove.contains(spark.getID())){
				continue;
			}

			if(Koru.basis.getSpark(spark.getID()) == null){
				spark.add();
			}
		}

		if(connected && Timers.get("ping", pingInterval)){
			client.updateReturnTripTime();
		}

		Array<Spark> sparks = Koru.basis.getSparks();

		//unloads sparks that are very far away
		for(Spark e : sparks){
			Spark spark = (Spark) e;
			if(spark.getType().callEvent(true, Unload.class) && spark.pos().rdist(player.pos().x, player.pos().y) > sparkUnloadRange){
				Koru.basis.removeSpark(e);
			}
		}

		tempids.clear();

		int id = -1;
		for(IntSetIterator rit = sparksToRemove.iterator(); rit.hasNext;){
			id = rit.next();
			if(Koru.basis.removeSpark(id)){
				tempids.add(id);
			}
		}

		for(int i = 0; i < tempids.size; i++){
			sparksToRemove.remove(tempids.get(i));
		}

		if(chunksAdded){
			renderer.updateTiles();
			chunksAdded = false;
		}

		if(connected && Timers.get("nupdate", packetFrequency)){
			sendUpdate();
		}
	}

	private void sendUpdate(){
		if(player.get(InputTrait.class).input == null)
			return;

		PositionPacket pos = new PositionPacket();
		pos.x = player.pos().x;
		pos.y = player.pos().y;
		pos.mouseangle = Angles.mouseAngle(Core.camera, player.pos().x, player.pos().y);
		player.get(InputTrait.class).input.mouseangle = pos.mouseangle;
		pos.direction = player.get(DirectionTrait.class).direction;

		client.sendUDP(pos);
	}
	
	@Override
	public void send(Object object, Mode mode){
		if(mode == Mode.TCP){
			client.sendTCP(object);
		}else{
			client.sendTCP(object);
		}
	}

	@Override
	public void sendRange(Object object, float x, float y, float range, Mode mode){
		throw new RuntimeException("Not supported on client!");
	}

	@Override
	public void sendTo(int clientid, Object object, Mode mode){
		throw new RuntimeException("Not supported on client!");
	}

	@Override
	public void sendExcept(int clientid, Object object, Mode mode){
		throw new RuntimeException("Not supported on client!");
	}

	@Override
	public void sendSpark(Spark spark){
		throw new RuntimeException("Not supported on client!");
	}

	@Override
	public void removeSpark(Spark spark){
		spark.remove();
	}

	class Listen extends Listener{
		@Override
		public void connected(Connection connection){
			ConnectPacket packet = new ConnectPacket();
			packet.name = player.get(ConnectionTrait.class).name;
			client.sendTCP(packet);
			Koru.log("Sent packet.");

			connected = true;
			Koru.ui.hideConnect();
			Koru.control.setState(GameState.playing);
		}

		@Override
		public void disconnected(Connection connection){
			disconnect();
			Gdx.app.postRunnable(() -> {
				Koru.ui.showError("Disconnected from server: timed out.");
			});

			Koru.control.setState(GameState.title);
		}

		@Override
		public void received(Connection c, Object object){
			try{
				Net.onRecieve(object);
			}catch(Exception e){
				e.printStackTrace();
				Koru.log("Packet recieve error!");
			}
		}
	}
}
