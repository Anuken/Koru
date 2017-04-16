package io.anuke.koru.modules;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import com.esotericsoftware.kryonet.*;

import io.anuke.koru.Koru;
import io.anuke.koru.components.*;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.BitmapData;
import io.anuke.koru.network.Registrator;
import io.anuke.koru.network.packets.*;
import io.anuke.koru.systems.CollisionSystem;
import io.anuke.koru.utils.Profiler;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Timers;

public class Network extends Module<Koru>{
	public static final String ip = System.getProperty("user.name").equals("anuke") ? "localhost" : "107.11.42.20";
	public static final int port = 7575;
	public static final int ping = 0;
	public static final int pingInterval = 60;
	public static final int packetFrequency = 3;
	public static final float entityUnloadRange = 600;
	
	public boolean initialconnect = false;
	public boolean connecting;
	public Client client;
	
	private boolean connected;
	private String lastError;
	private boolean chunksAdded = false;
	private LongArray tempids = new LongArray();
	private Array<KoruEntity> entityQueue = new Array<KoruEntity>();
	private ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	private ObjectSet<Long> requestedEntities = new ObjectSet<Long>();
	private ObjectMap<Integer, BitmapData> bitmaps = new ObjectMap<Integer, BitmapData>();
	private ObjectMap<Class<?>, Consumer<Object>> packetHandlers = new ObjectMap<>();
	
	
	@Override
	public void init(){
		registerPackets();
		
		int buffer = (int) Math.pow(2, 6) * 8192;
		client = new Client(buffer, buffer);
		Registrator.register(client.getKryo());
		client.addListener(new Listen());
		client.start();
	}
	
	private void registerPackets(){
		
		handle(DataPacket.class, data->{
			Koru.log("Recieving a data packet... ");

			main.engine.removeAllEntities();
			Koru.log("Recieved " + data.entities.size() + " entities.");
			for(Entity entity : data.entities){
				entityQueue.add((KoruEntity) entity);
			}
			Koru.getEngine().getSystem(CollisionSystem.class).getColliderEngine().getAllColliders().clear();
			getModule(World.class).time = data.time;
			getModule(ClientData.class).player.collider().init = false;
			getModule(ClientData.class).player.resetID(data.playerid);
			entityQueue.add(getModule(ClientData.class).player);
			Koru.log("Recieved data packet.");
		});
		
		handle(WorldUpdatePacket.class,p->{
			for(Long key : p.updates.keys()){
				KoruEntity entity = main.engine.getEntity(key);
				if(entity == null){
					requestEntity(key);
					continue;
				}
				entity.get(SyncComponent.class).type.read(p.updates.get(key), entity);
			}
		});
		
		handle(ChunkPacket.class,p->{
			getModule(World.class).loadChunks(p);
			chunksAdded = true;
		});
		
		handle(TileUpdatePacket.class,p->{
			if(getModule(World.class).inClientBounds(p.x, p.y))
				getModule(World.class).setTile(p.x, p.y, p.tile);
			chunksAdded = true;
		});
		
		handle(MenuOpenPacket.class,p->{
			Gdx.app.postRunnable(() -> {
				getModule(UI.class).openMenu(p.type);
			});
		});
		
		handle(SlotChangePacket.class,p->{
			if(main.engine.getEntity(p.id) == null)
				return;
			main.engine.getEntity(p.id).getComponent(InventoryComponent.class).inventory[0][0] = p.stack;
		});
		
		handle(InventoryUpdatePacket.class,p->{
			getModule(ClientData.class).player.getComponent(InventoryComponent.class).set(p.stacks, p.selected);
		});
		
		handle(AnimationPacket.class,p->{
			main.engine.getEntity(p.player).getComponent(RenderComponent.class).renderer.onAnimation(p.type);
		});
		
		handle(ChatPacket.class,p->{
			Gdx.app.postRunnable(() -> {
				getModule(UI.class).chat.addMessage(p.message, p.sender);
			});
		});
		
		handle(KoruEntity.class,entity->{
			entityQueue.add(entity);
		});
		
		handle(EntityRemovePacket.class,p->{
			entitiesToRemove.add(p.id);
		});

	}
	
	private <T> void handle(Class<T> c, Consumer<T> cons){
		packetHandlers.put(c, (Consumer<Object>)cons);
	}

	public void connect(){
		try{

			connecting = true;
			client.connect(1200, ip, port, port);
			Koru.log("Connecting to server..");
			ConnectPacket packet = new ConnectPacket();
			packet.name = getModule(ClientData.class).player.connection().name;
			client.sendTCP(packet);
			Koru.log("Sent packet.");

			connected = true;
		}catch(Exception e){
			connecting = false;
			connected = false;
			e.printStackTrace();
			lastError = "Failed to connect to server:\n" + (e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			Koru.log("Connection failed!");
		}

		connecting = false;
		initialconnect = true;
	}

	void requestEntity(long id){
		if(!requestedEntities.contains(id) && !entitiesToRemove.contains(id)){
			requestedEntities.add(id);

			EntityRequestPacket request = new EntityRequestPacket();
			request.id = id;
			client.sendTCP(request);
		}
	}

	@Override
	public void update(){
		long start = TimeUtils.nanoTime();
		
		if(connected && !client.isConnected()){
			connected = false;
			connecting = false;
			lastError = "Connection error: Timed out.";

			//reset everything.

			World world = getModule(World.class);

			for(int x = 0; x < world.chunks.length; x++){
				for(int y = 0; y < world.chunks[x].length; y++){
					world.chunks[x][y] = null;
				}
			}

			main.engine.removeAllEntities();
		}
		
		while(entityQueue.size != 0){

			KoruEntity entity = entityQueue.pop();
			if(entity == null)
				continue;

			requestedEntities.remove(entity.getID());

			if(entitiesToRemove.contains(entity.getID())){
				entitiesToRemove.remove(entity.getID());
				continue;
			}

			if(main.engine.getEntity(entity.getID()) == null)
				entity.add();
		}

		if(connected && Timers.get("ping", pingInterval)){
			client.updateReturnTripTime();
		}
		
		KoruEntity player = getModule(ClientData.class).player;
		ImmutableArray<Entity> entities = main.engine.getEntities();

		//unloads entities that are very far away
		for(Entity e : entities){
			KoruEntity entity = (KoruEntity) e;
			if(entity.getType().unload() && entity.position().sqdist(player.getX(), player.getY()) > entityUnloadRange){
				main.engine.removeEntity(e);
			}
		}

		tempids.clear();
		
		for(Long id : entitiesToRemove){
			if(main.engine.removeEntity(id)){
				tempids.add(id);
			}
		}
		
		for(int i = 0; i < tempids.size; i ++)
			entitiesToRemove.remove(tempids.get(i));
		
		if(chunksAdded){
			getModule(Renderer.class).updateTiles();
			chunksAdded = false;
		}

		if(connected && Timers.get("nupdate", packetFrequency))
			sendUpdate();
		
		if(Profiler.update())
			Profiler.networkTime = TimeUtils.timeSinceNanos(start);
	}

	private void sendUpdate(){
		KoruEntity player = getModule(ClientData.class).player;
		
		PositionPacket pos = new PositionPacket();
		pos.x = player.getX();
		pos.y = player.getY();
		pos.mouseangle = Angles.mouseAngle(getModule(Renderer.class).camera, player.getX(), player.getY());
		getModule(ClientData.class).player.get(InputComponent.class).input.mouseangle = pos.mouseangle;
		pos.direction = player.getComponent(RenderComponent.class).direction;
		pos.velocity = player.collider().collider.getVelocity();
		
		client.sendUDP(pos);
	}

	public String getError(){
		return lastError;
	}

	public boolean connected(){
		return connected;
	}

	public boolean initialconnect(){
		return initialconnect;
	}
	
	class Listen extends Listener{
		@Override
		public void received(Connection c, Object object){
			try{
				if(packetHandlers.containsKey(object.getClass())){
					packetHandlers.get(object.getClass()).accept(object);
				}else if(!(object instanceof FrameworkMessage)){
					Koru.log("Unhandled packet type: " + object.getClass().getSimpleName());
				}
			}catch(Exception e){
				e.printStackTrace();
				Koru.log("Packet recieve error!");
			}
		}
	}
}
