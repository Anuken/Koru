package io.anuke.koru.modules;

import static io.anuke.ucore.UCore.inBounds;

import java.util.Collection;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.ChunkPacket;
import io.anuke.koru.network.packets.ChunkRequestPacket;
import io.anuke.koru.network.packets.TileUpdatePacket;
import io.anuke.koru.systems.SyncSystem;
import io.anuke.koru.utils.Profiler;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.WorldLoader;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.StructMaterialTypes;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.modules.Module;

public class World extends Module<Koru>{
	public static final int chunksize = 16;
	public static final int loadrange = 3;
	public static final int tilesize = 12;
	
	private static Color ambientColor = new Color();
	private static final float[] colors = new float[]{1, 1, 0.9f, 0.5f, 0.2f, 0, 0, 0.5f, 0.9f, 1};
	public final static float timescale = 40000f*0; //temporarily disabled for testing - remove 0 to enable
	
	private boolean updated;
	private GridPoint2 point = new GridPoint2();
	private WorldLoader file;
	private Renderer renderer;
	private Network network;
	private boolean[][] chunkloaded;
	
	public int lastchunkx, lastchunky;
	public Chunk[][] chunks; //client-side tiles
	public Chunk[][] tempchunks; //temporary operation chunks
	public float time = 0f; //world time
	
	public World(WorldLoader loader){
		this();
		file = loader;
	}

	public World(){
		
		if( !IServer.active()){
			chunkloaded = new boolean[loadrange * 2][loadrange * 2];
			chunks = new Chunk[loadrange * 2][loadrange * 2];
			tempchunks = new Chunk[loadrange * 2][loadrange * 2];
		}
	}
	
	public void init(){
		network = getModule(Network.class);
		renderer = getModule(Renderer.class);
	}
	
	//TODO move time and color related function to a different class?
	public Color getAmbientColor(){
		int index = (int)(time*colors.length);
		float mod = time*colors.length-index;
		float current = colors[index];
		float next = colors[(index == colors.length-1 ?  0 : index+1)];
		
		return Hue.mix(Color.WHITE, Color.BLACK, 1f-(current*(1f-mod) + next*mod), ambientColor);
	}

	@Override
	public void update(){
		long start = TimeUtils.nanoTime();
		
		if(IServer.active() && IServer.instance().getFrameID() % 60 == 0) checkUnloadChunks();
	
		time += !IServer.active() ? Gdx.graphics.getDeltaTime()*60f/timescale : IServer.instance().getDelta()/timescale;
		if(time >= 1f) time = 0f;
		
		updated = false;
		
		if(IServer.active()) return;

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
					if(!inBounds(x + sx, y + sy, chunks)){
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
		
		if(Profiler.update())
			Profiler.worldTime = TimeUtils.timeSinceNanos(start);
	}
	
	void checkUnloadChunks(){
		Collection<Chunk> chunks = file.getLoadedChunks();
		ImmutableArray<Entity> players = IServer.instance().getEngine().getEntitiesFor(IServer.instance().getEngine().getSystem(SyncSystem.class).getFamily());
		
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
			file.unloadChunk(chunk);
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
		packet.chunk = file.getChunk(request.x, request.y);
		return packet;
	}

	public static World instance(){
		return IServer.active() ? IServer.instance().getWorld() : Koru.module(World.class);
	}

	public boolean positionSolid(float x, float y){
		Tile tile = getWorldTile(x, y);
		Material block = tile.block();
		Material tilem = tile.topTile();
		return (block.getType().solid() && block.getType().getHitbox(tile(x), tile(y), Rectangle.tmp).contains(x, y)) || (tilem.getType().solid() && tilem.getType().getHitbox(tile(x), tile(y), Rectangle.tmp).contains(x, y));
	}

	public boolean blockSolid(int x, int y){
		return getTile(x, y).solid();
	}

	public boolean isAccesible(int x, int y){
		return !blockSolid(x - 1, y) || !blockSolid(x + 1, y) || !blockSolid(x, y - 1) || !blockSolid(x, y + 1);
	}

	public boolean blends(int x, int y, Material material){
		return !isType(x, y + 1, material) || !isType(x, y - 1, material) || !isType(x + 1, y, material) || !isType(x - 1, y, material);
	}

	public boolean isType(int x, int y, Material material){
		if( !inClientBounds(x, y)){
			return true;
		}
		return getTile(x, y).block() == material || getTile(x, y).topTile() == material;
	}

	public GridPoint2 search(Material material, int x, int y, int range){
		float nearest = Float.MAX_VALUE;
		for(int cx = -range;cx <= range;cx ++){
			for(int cy = -range;cy <= range;cy ++){
				int worldx = x + cx;
				int worldy = y + cy;
				if(getTile(worldx, worldy).block() == material || getTile(worldx, worldy).topTile() == material){
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

	public boolean inClientBounds(int x, int y){
		if(IServer.active()) return true;
		int tx = tile(renderer.camera.position.x);
		int ty = tile(renderer.camera.position.y);
		if(Math.abs(tx - x) >= loadrange * chunksize - 1 || Math.abs(ty - y) >= loadrange * chunksize - 1) return false;
		int ax = x / chunksize - tx / chunksize + loadrange;
		int ay = y / chunksize - ty / chunksize + loadrange;
		if( !inBounds(ax, ay, chunks)){ 
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
		if(!inBounds(ax, ay, chunks)) return null;
		return chunks[ax][ay];
	}

	public void updateTile(int x, int y){
		updated = true;
		getTile(x, y).changeEvent();
		IServer.instance().sendToAllIn(new TileUpdatePacket(x, y, getTile(x, y)), world(x), world(y), tilesize*chunksize*(1+loadrange));
	}

	public void updateLater(int x, int y){
		updated = true;
		getTile(x, y).changeEvent();
		IServer.instance().sendLater(new TileUpdatePacket(x, y, getTile(x, y)));
	}

	public Tile getTile(int x, int y){
		if( !IServer.active()){
			Chunk chunk = getRelativeChunk(x, y);
			return chunk == null ? null : chunk.getWorldTile(x, y);
		}
		int cx = (x < -1 ? x + 1 : x) / chunksize, cy = (y < -1 ? y + 1 : y) / chunksize;
		if(x < 0) cx --;
		if(y < 0) cy --;
		return file.getChunk(cx, cy).getWorldTile(x, y);
	}
	
	public Tile getTile(GridPoint2 point){
		return getTile(point.x, point.y);
	}
	
	public Tile getTile(long c){
		int x = (int)(c >> 32);
		int y = (int)c;
		return getTile(x,y);
	}

	public Tile getWorldTile(float fx, float fy){
		int x = tile(fx);
		int y = tile(fy);
		return getTile(x, y);
	}
	
	public void setTile(int x, int y, Tile tile){
		if( !IServer.active()){
			getRelativeChunk(x, y).setWorldTile(x, y, tile);
			return;
		}
		int cx = x / chunksize, cy = y / chunksize;
		file.getChunk(cx, cy).setWorldTile(x, y, tile);
	}

	public int toChunkCoords(int a){
		return (a / chunksize);
	}

	public int toChunkCoords(float worldpos){
		int i = tile(worldpos) / chunksize;
		return i;
	}

	public boolean updated(){
		return updated;
	}

	public static int tile(float i){
		return nint(i/tilesize);
	}
	
	public static float world(int i){
		return tilesize * i + tilesize / 2;
	}
	
	static int nint(float b){
		return b < 0 ? (int)(b-1) : (int)b;
	}
	
	static long getLong(int x, int y){
		return (long)x << 32 | y & 0xFFFFFFFFL;
	}
	
	static int getX(long l){
		return(int)(l >> 32);
	}
	
	static int getY(long l){
		return (int)l;
	}
	
	//TODO make this work with transparency instead of specific types
	public static boolean isPlaceable(Material material, Tile tile){
		
		if(!material.getType().tile()){
			return (tile.blockEmpty());
		}else{
			return tile.topTile() != material && 
				(tile.blockEmpty() || tile.block().getType() == StructMaterialTypes.torch) && tile.canAddTile();
		}		
	}
}
