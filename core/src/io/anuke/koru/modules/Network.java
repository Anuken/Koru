package io.anuke.koru.modules;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.koru.Koru;
import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.components.SyncComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.BitmapData;
import io.anuke.koru.network.IClient;
import io.anuke.koru.network.NetworkListener;
import io.anuke.koru.network.packets.BitmapDataPacket;
import io.anuke.koru.network.packets.ChatPacket;
import io.anuke.koru.network.packets.ChunkPacket;
import io.anuke.koru.network.packets.ConnectPacket;
import io.anuke.koru.network.packets.DataPacket;
import io.anuke.koru.network.packets.EntityRemovePacket;
import io.anuke.koru.network.packets.EntityRequestPacket;
import io.anuke.koru.network.packets.GeneratedMaterialPacket;
import io.anuke.koru.network.packets.InventoryUpdatePacket;
import io.anuke.koru.network.packets.PositionPacket;
import io.anuke.koru.network.packets.SlotChangePacket;
import io.anuke.koru.network.packets.TileUpdatePacket;
import io.anuke.koru.network.packets.WorldUpdatePacket;
import io.anuke.koru.utils.Angles;
import io.anuke.ucore.modules.Module;

public class Network extends Module<Koru>{
	public static final String ip = System.getProperty("user.name").equals("anuke") ? "localhost" : "107.11.42.20";
	public static final int port = 7575;
	public static final int ping = 0;
	public static final int pingInterval = 60;
	public static final int packetFrequency = 3;
	public static final float entityUnloadRange = 600;
	public boolean initialconnect = false;
	public boolean connecting;
	private boolean connected;
	private String lastError;
	private boolean chunksAdded = false;
	private Array<Long> tempids = new Array<Long>();
	private Array<KoruEntity> entityQueue = new Array<KoruEntity>();
	private ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	private ObjectSet<Long> requestedEntities = new ObjectSet<Long>();
	private ObjectMap<Integer, BitmapData> bitmaps = new ObjectMap<Integer, BitmapData>();
	public IClient client;

	public void init(){
		client.addListener(new Listen());
	}

	public void connect(){
		new Thread(() -> {
			try{
				connecting = true;
				client.connect(ip, port);
				Koru.log("Connecting to server..");
				ConnectPacket packet = new ConnectPacket();
				packet.name = getModule(ClientData.class).player.getComponent(ConnectionComponent.class).name;
				client.sendTCP(packet);
				Koru.log("Sent packet.");

				connected = true;
			}catch(Exception e){
				connecting = false;
				connected = false;
				e.printStackTrace();
				lastError = "Failed to connect to server:\n" + e.getCause().getMessage();
				Koru.log("Connection failed!");
			}
		}).start();

		connecting = false;
		initialconnect = true;
	}

	class Listen extends NetworkListener{
		@Override
		public void received(Object object){
			try{
				if(object instanceof DataPacket){
					Koru.log("Recieving a data packet... ");
					DataPacket data = (DataPacket) object;

					t.engine.removeAllEntities();
					Koru.log("Recieved " + data.entities.size() + " entities.");
					for(Entity entity : data.entities){
						entityQueue.add((KoruEntity) entity);
					}
					getModule(World.class).time = data.time;
					getModule(ClientData.class).player.resetID(data.playerid);
					entityQueue.add(getModule(ClientData.class).player);
					Koru.log("Recieved data packet.");
				}else if(object instanceof WorldUpdatePacket){
					WorldUpdatePacket packet = (WorldUpdatePacket) object;
					for(Long key : packet.updates.keys()){
						KoruEntity entity = t.engine.getEntity(key);
						if(entity == null){
							requestEntity(key);
							continue;
						}
						entity.mapComponent(SyncComponent.class).type.read(packet.updates.get(key), entity);
					}
				}else if(object instanceof ChunkPacket){
					ChunkPacket packet = (ChunkPacket) object;
					getModule(World.class).loadChunks(packet);
					chunksAdded = true;
				}else if(object instanceof TileUpdatePacket){
					TileUpdatePacket packet = (TileUpdatePacket) object;
					if(getModule(World.class).inBounds(packet.x, packet.y))
						getModule(World.class).setTile(packet.x, packet.y, packet.tile);
					chunksAdded = true;
				}else if(object instanceof EntityRemovePacket){
					EntityRemovePacket packet = (EntityRemovePacket) object;
					entitiesToRemove.add(packet.id);
				}else if(object instanceof SlotChangePacket){
					SlotChangePacket packet = (SlotChangePacket) object;
					if(t.engine.getEntity(packet.id) == null) return;
					t.engine.getEntity(packet.id).getComponent(InventoryComponent.class).inventory[0][0] = packet.stack;
				}else if(object instanceof ChatPacket){
					ChatPacket packet = (ChatPacket) object;
					Gdx.app.postRunnable(() -> {
						getModule(UI.class).chat.addMessage(packet.message, packet.sender);
					});
				}else if(object instanceof KoruEntity){
					KoruEntity entity = (KoruEntity) object;
					entityQueue.add(entity);
				}else if(object instanceof InventoryUpdatePacket){
					InventoryUpdatePacket packet = (InventoryUpdatePacket) object;
					getModule(ClientData.class).player.getComponent(InventoryComponent.class).set(packet.stacks, packet.selected);
				}else if(object instanceof BitmapDataPacket.Header){
					BitmapDataPacket.Header packet = (BitmapDataPacket.Header) object;
					Koru.log("Recieved bitmap header: " + packet.id + " [" + packet.width + "x" + packet.height + "]");
					BitmapData data = new BitmapData(packet.width, packet.height, packet.colors);
					bitmaps.put(packet.id, data);
				}else if(object instanceof BitmapDataPacket){
					BitmapDataPacket packet = (BitmapDataPacket) object;
					Koru.log("Recieved split bitmap: " + packet.id + " [" + packet.data.length + " bytes]");
					bitmaps.get(packet.id).pushBytes(packet.data);
					if(bitmaps.get(packet.id).isDone()){
						Gdx.app.postRunnable(() -> {
							getModule(ObjectHandler.class).bitmapRecieved(packet.id, bitmaps.get(packet.id));
							bitmaps.remove(packet.id);
						});
					}
				}else if(object instanceof GeneratedMaterialPacket){
					GeneratedMaterialPacket packet = (GeneratedMaterialPacket) object;
					Gdx.app.postRunnable(() -> {
						getModule(ObjectHandler.class).materialPacketRecieved(packet);
					});
				}
			}catch(Exception e){
				e.printStackTrace();
				Koru.log("Packet recieve error!");
			}
		}
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
		
		if(connected && !client.isConnected()){
			connected = false;
			connecting = false;
			lastError = "Connection error: Timed out.";
			
			//reset everything.
			
			World world = getModule(World.class);
			
			for(int x = 0; x < world.chunks.length; x ++){
				for(int y = 0; y < world.chunks[x].length; y ++){
					world.chunks[x][y] = null;
				}
			}
			
			t.engine.removeAllEntities();
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
			
			if(t.engine.getEntity(entity.getID()) == null)
			entity.addSelf();
		}
		
		if(connected && Gdx.graphics.getFrameId() % pingInterval == 0){
			client.updatePing();
		}
		
		KoruEntity player = getModule(ClientData.class).player;
		ImmutableArray<Entity> entities = t.engine.getEntities();
		
		//unloads entities that are very far away
		for(Entity e : entities){
			KoruEntity entity = (KoruEntity)e;
			if(entity.getType().unload() && entity.position().sqdist(player.getX(), player.getY()) > entityUnloadRange){
				t.engine.removeEntity(e);
			}
		}
		
		tempids.clear();
		
		for(Long id : entitiesToRemove){
			if(t.engine.removeEntity(id)){
				tempids.add(id);
			}
		}
		
		for(Long l : tempids)
			entitiesToRemove.remove(l);

		if(chunksAdded){
			getModule(Renderer.class).updateTiles();
			chunksAdded = false;
		}

		if(Gdx.graphics.getFrameId() % packetFrequency == 0)
			sendUpdate();
	}

	private void sendUpdate(){
		PositionPacket pos = new PositionPacket();
		pos.x = getModule(ClientData.class).player.mapComponent(PositionComponent.class).x;
		pos.y = getModule(ClientData.class).player.mapComponent(PositionComponent.class).y;
		pos.mouseangle = Angles.mouseAngle(getModule(Renderer.class).camera, getModule(ClientData.class).player.getX(),
				getModule(ClientData.class).player.getY());
		pos.direction = getModule(ClientData.class).player.getComponent(RenderComponent.class).direction;
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
}
