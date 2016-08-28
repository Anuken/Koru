package net.pixelstatic.koru.world;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.Network;
import net.pixelstatic.koru.modules.Renderer;
import net.pixelstatic.koru.network.packets.ChunkPacket;
import net.pixelstatic.koru.network.packets.ChunkRequestPacket;
import net.pixelstatic.koru.network.packets.TileUpdatePacket;
import net.pixelstatic.koru.server.KoruServer;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.systems.SyncSystem;
import net.pixelstatic.koru.utils.Point;
import net.pixelstatic.utils.DirectionUtils;
import net.pixelstatic.utils.MiscUtils;
import net.pixelstatic.utils.modules.Module;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

public class World extends Module<Koru>{
	public static final int chunksize = 16;
	public static final int loadrange = 2;
	public static final int tilesize = 12;
	public int lastchunkx, lastchunky;
	private static Rectangle rect = new Rectangle();
	private boolean updated;
	private Point point = new Point();
	private WorldFile file;
	private Renderer renderer;
	public Generator generator;
	Network network;
	KoruServer server;
	public Chunk[][] chunks; //client-side tiles
	public Chunk[][] tempchunks; //temporary operation chunks
	boolean[][] chunkloaded;
	private ConcurrentHashMap<Long, Chunk> loadedchunks = new ConcurrentHashMap<Long, Chunk>(); //server-side chunks

	public World(){
		if( !KoruServer.active){
			chunkloaded = new boolean[loadrange * 2][loadrange * 2];
			chunks = new Chunk[loadrange * 2][loadrange * 2];
			tempchunks = new Chunk[loadrange * 2][loadrange * 2];
		}else{
			file = new WorldFile(Paths.get("world"));
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
		    @Override
		    public void run(){
		    	if(!KoruServer.active) return;
		    	Koru.log("Saving " + loadedchunks.size() +" chunks...");
		    	for(Chunk chunk : loadedchunks.values()){
		    		file.writeChunk(chunk);
		    	}
		    }
		});
	}
	
	public World(KoruServer server){
		this();
		this.server = server;
	}

	@Override
	public void update(){
		if(server != null && KoruUpdater.frameID() % 60 == 0) checkUnloadChunks();
		updated = false;
		
		
		if(server != null) return;

		int newx = toChunkCoords(renderer.camera.position.x);
		int newy = toChunkCoords(renderer.camera.position.y);

		//camera moved, update chunks
		if(newx != lastchunkx || newy != lastchunky){
			
			int sx = newx - lastchunkx, sy = newy - lastchunky;
			
			for(int x = 0;x < loadrange * 2;x ++){
				for(int y = 0;y < loadrange * 2;y ++){
					tempchunks[x][y] = chunks[x][y];
				//	if(chunks[x][y] == null) continue;
					//Pools.free(chunks[x][y]);
					//chunks[x][y] = null;
				}
			}
			
			for(int x = 0;x < loadrange * 2;x ++){
				for(int y = 0;y < loadrange * 2;y ++){
					if(!MiscUtils.inBounds(x + sx, y + sy, chunks)){
						chunks[x][y] = null;
						continue;
					}
					chunks[x][y] = tempchunks[x + sx][y + sy];
				}
			}
		}

		lastchunkx = newx;
		lastchunky = newy;

		sendChunkRequest();
	}
	
	void checkUnloadChunks(){
		Collection<Chunk> chunks = loadedchunks.values();
		ImmutableArray<Entity> players = KoruUpdater.instance.engine.getEntitiesFor(KoruUpdater.instance.engine.getSystem(SyncSystem.class).getFamily());
		
		for(Chunk chunk : chunks){
			boolean passed = false;
			for(Entity e : players){
				KoruEntity entity = (KoruEntity)e;
				int ecx = toChunkCoords(entity.position().x);
				int ecy = toChunkCoords(entity.position().y);
				
				if(Math.abs(chunk.x - ecx) <= loadrange && Math.abs(chunk.y - ecy) <= loadrange){
					passed = true;
					break;
				}
			}
			if(passed) continue;
			unloadChunk(chunk);
		}
	}

	public void loadChunks(ChunkPacket packet){

		//the relative position of the packet's chunk, to be put in the client's chunk array
		int relativex = packet.chunk.x - lastchunkx + loadrange;
		int relativey = packet.chunk.y - lastchunky + loadrange;
		
		//if the chunk coords are out of range, stop
		if(relativex < 0 || relativey < 0 || relativex >= loadrange * 2 || relativey >= loadrange * 2){
			return;
		}
		
		chunks[relativex][relativey] = packet.chunk;
	}

	public void sendChunkRequest(){
		for(int x = 0;x < loadrange * 2;x ++){
			for(int y = 0;y < loadrange * 2;y ++){
				if(chunks[x][y] == null){
					ChunkRequestPacket packet = new ChunkRequestPacket();
					packet.x = lastchunkx + x - loadrange;
					packet.y = lastchunky + y - loadrange;
					network.client.sendTCP(packet);
				}
			}
		}
	}

	public synchronized ChunkPacket createChunkPacket(ChunkRequestPacket request){
		ChunkPacket packet = new ChunkPacket();
		packet.chunk = getChunk(request.x, request.y);
		return packet;
	}

	public static World instance(){
		return KoruUpdater.instance.world;
	}

	public Tile getTile(Point point){
		return tile(point.x, point.y);
	}

	public Tile getTile(float fx, float fy){
		int x = tile(fx);
		int y = tile(fy);
		return tile(x, y);
	}

	public Point findEmptySpace(int x, int y){
		//Structure structure = entity.groupc().structure;
		//	structure.getBuildState(x, y);
		for(int k = -1;k < 3;k ++){
			int i = (k + 4) % 4;
			int sx = DirectionUtils.toX(i), sy = DirectionUtils.toY(i);
			if( !blockSolid(x + sx, y + sy) /*&& structure.getBuildState(sx + x, sy + y) == BuildState.completed*/){
				point.set(x + sx, y + sy);
				return point;
			}
		}
		return null;
	}

	public boolean positionSolid(float x, float y){
		Tile tile = getTile(x, y);
		return (tile.block.getType().solid() && tile.block.getType().getRect(tile(x), tile(y), rect).contains(x, y)) || (tile.tile.getType().solid() && tile.tile.getType().getRect(tile(x), tile(y), rect).contains(x, y));
	}

	public boolean blockSolid(int x, int y){
		return tile(x, y).solid();
	}

	public boolean isAccesible(int x, int y){
		return !blockSolid(x - 1, y) || !blockSolid(x + 1, y) || !blockSolid(x, y - 1) || !blockSolid(x, y + 1);
	}

	public boolean blends(int x, int y, Material material){
		return !isType(x, y + 1, material) || !isType(x, y - 1, material) || !isType(x + 1, y, material) || !isType(x - 1, y, material);
	}

	public boolean isType(int x, int y, Material material){
		if( !inBounds(x, y)){
			return true;
		}
		return tile(x, y).tile == material;
	}

	public Point search(Material material, int x, int y, int range){
		float nearest = Float.MAX_VALUE;
		for(int cx = -range;cx <= range;cx ++){
			for(int cy = -range;cy <= range;cy ++){
				int worldx = x + cx;
				int worldy = y + cy;
				if(tile(worldx, worldy).block == material || tile(worldx, worldy).tile == material){
					float dist = Vector2.dst(x, y, worldx, worldy);
					if(dist < nearest){
						point.set(worldx, worldy);
						nearest = dist;
						return point;
					}
				}
			}
		}
		if(nearest > 0) return point;

		return null;
	}

	public boolean inBounds(int x, int y){
		if(KoruServer.active) return true;
		int tx = tile(renderer.camera.position.x);
		int ty = tile(renderer.camera.position.y);
		if(Math.abs(tx - x) >= loadrange * chunksize - 1 || Math.abs(ty - y) >= loadrange * chunksize - 1) return false;
		int ax = x / chunksize - tx / chunksize + loadrange;
		int ay = y / chunksize - ty / chunksize + loadrange;
		if( !MiscUtils.inBounds(ax, ay, chunks)){ 
			return false;
		}
		if(getRelativeChunk(x, y) == null) return false;
		return true;
	}

	public Chunk getRelativeChunk(int x, int y){
		if(x < -1) x ++;
		if(y < -1) y ++;
		int ax = nint((float)x / chunksize) - lastchunkx + loadrange;
		int ay = nint((float)y / chunksize) - lastchunky + loadrange;
		if(!MiscUtils.inBounds(ax, ay, chunks)) return null;
		return chunks[ax][ay];
	}

	public void updateTile(int x, int y){
		updated = true;
		tile(x, y).changeEvent();
		server.sendToAll(new TileUpdatePacket(x, y, tile(x, y)));
	}

	protected Chunk generateChunk(int chunkx, int chunky){
		Chunk chunk = Pools.obtain(Chunk.class);
		chunk.set(chunkx, chunky);
		generator.generateChunk(chunk);
		loadedchunks.put(hashCoords(chunkx, chunky), chunk);
		
		//file.writeChunk(chunk);
		return chunk;
	}

	protected void unloadChunk(Chunk chunk){
		file.writeChunk(chunk);
		loadedchunks.remove(hashCoords(chunk.x, chunk.y));
	}

	public Chunk getChunk(int chunkx, int chunky){
		Chunk chunk = loadedchunks.get(hashCoords(chunkx, chunky));
		if(chunk == null){
			if(file.chunkIsSaved(chunkx, chunky)){
				Chunk schunk = file.readChunk(chunkx, chunky);
				loadedchunks.put(hashCoords(chunkx, chunky), schunk);
				return schunk;
			}else{
				return generateChunk(chunkx, chunky);
			}
		}
		return chunk;
	}

	public Tile tile(int x, int y){
		if( !KoruServer.active){
			return getRelativeChunk(x, y).getWorldTile(x, y);
		}
		int cx = (x < -1 ? x + 1 : x) / chunksize, cy = (y < -1 ? y + 1 : y) / chunksize;
		if(x < 0) cx --;
		if(y < 0) cy --;
		return getChunk(cx, cy).getWorldTile(x, y);
	}
	
	public void setTile(int x, int y, Tile tile){
		if( !KoruServer.active){
			getRelativeChunk(x, y).setWorldTile(x, y, tile);
			return;
		}
		int cx = x / chunksize, cy = y / chunksize;
		getChunk(cx, cy).setWorldTile(x, y, tile);
	}

	public int toChunkCoords(int a){
		return (a / chunksize);
	}

	public int toChunkCoords(float worldpos){
		int i = tile(worldpos) / chunksize;
		return i;
	}

	public static long hashCoords(int a, int b){
		return (((long)a) << 32) | (b & 0xffffffffL);
	}

	public boolean updated(){
		return updated;
	}

	public static int tile(float i){
		return nint(i/tilesize);
	}
	
	public static int nint(float b){
		return b < 0 ? (int)(b-1) : (int)b;
	}

	public static float world(int i){
		return tilesize * i + tilesize / 2;
	}
	
	public int totalChunks(){
		return file.totalChunks();
	}

	public void init(){
		network = getModule(Network.class);
		renderer = getModule(Renderer.class);
	}
}
