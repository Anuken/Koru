package net.pixelstatic.koru.world;


import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.ai.AIData;
import net.pixelstatic.koru.modules.Module;
import net.pixelstatic.koru.modules.Network;
import net.pixelstatic.koru.modules.Renderer;
import net.pixelstatic.koru.network.packets.ChunkPacket;
import net.pixelstatic.koru.network.packets.ChunkRequestPacket;
import net.pixelstatic.koru.network.packets.TileUpdatePacket;
import net.pixelstatic.koru.server.KoruServer;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.utils.Point;
import net.pixelstatic.utils.DirectionUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;

public class World extends Module{
	public static final int chunksize = 10;
	public static final int loadrange = 2;
	public static final int tilesize = 12;
	private static Rectangle rect = new Rectangle();
	private boolean updated;
	private Point point = new Point();
	private WorldFile file;
	public Generator generator;
	Network network;
	KoruServer server;
	public Chunk[][] chunks; //client-side tiles
	boolean[][] chunkloaded;
	private ObjectMap<Integer, Chunk> loadedchunks = new ObjectMap<Integer, Chunk>(); //server-side chunks
	
	public World(Koru k){
		super(k);
		if( !KoruServer.active){
			chunkloaded = new boolean[loadrange*2][loadrange*2];
			chunks = new Chunk[loadrange*2][loadrange*2];
		}else{
			file = new WorldFile(Gdx.files.local("world.kwf"));
		}
	}

	public World(){
		this(null);
	}
	
	@Override
	public void update(){
		updated = false;
		if(server != null) return;
		
		sendChunkRequest();
	}

	public void loadChunks(ChunkPacket packet){
		//camera position, in chunk coords
		int chunkx = toChunkCoords(getModule(Renderer.class).camera.position.x); 
		int	chunky = toChunkCoords(getModule(Renderer.class).camera.position.y);
		
		//the relative position of the packet's chunk, to be put in the client's chunk array
		int relativex = chunkx - packet.chunk.x + loadrange;
		int relativey = chunky - packet.chunk.y + loadrange;
		
		//if the chunk coords are out of range, stop
		if(relativex < 0 || relativey < 0 || relativex >= loadrange*2 || relativey >= loadrange*2) return;
		
		chunks[relativex][relativey] = packet.chunk;
	}

	public void sendChunkRequest(){
		float px = getModule(Renderer.class).camera.position.x;
		float py = getModule(Renderer.class).camera.position.y;
		int blockx = (int)(px / tilesize), blocky = (int)(py / tilesize);
		for(int x = 0; x < loadrange * 2; x ++){
			for(int y = 0; y < loadrange * 2; y ++){
				if(chunks[x][y] == null){
					ChunkRequestPacket packet = new ChunkRequestPacket();
					packet.x = blockx / chunksize + x - loadrange;
					packet.y = blocky / chunksize + y - loadrange;
					network.client.sendTCP(packet);
				}
			}
		}
		/*
		for(int cx = -loadrange;cx <= loadrange;cx ++){
			for(int cy = -loadrange;cy <= loadrange;cy ++){
				int chunkx = cx + blockx / chunksize, chunky = cy + blocky / chunksize;
				if(chunkx < 0 || chunky < 0 || chunkx >= worldwidth / chunksize || chunky >= worldheight / chunksize) continue;
				if(chunkloaded[chunkx][chunky]) continue;
				ChunkRequestPacket packet = new ChunkRequestPacket();
				packet.x = chunkx * chunksize;
				packet.y = chunky * chunksize;
				network.client.sendTCP(packet);
				/*
				int relativeblockx = cx * chunksize - chunksize/2, relativeblocky = cy * chunksize - chunksize/2;
				ChunkPacket packet = new ChunkPacket();
				packet.x = relativeblockx;
				packet.y = relativeblocky;
				packet.tiles = new Tile[2][chunksize][chunksize];
				for(int x = relativeblockx; x < relativeblockx + chunksize; x ++){
					for(int y = relativeblockx; y < relativeblockx + chunksize; y ++){
						
					}
				}
				//end?
				
			}
		}
		 */
	}

	public ChunkPacket createChunkPacket(ChunkRequestPacket request){
		ChunkPacket packet = new ChunkPacket();
		packet.chunk = this.getChunk(request.x, request.y);
		return packet;
		/*
		ChunkPacket chunk = new ChunkPacket();
		chunk.x = packet.x;
		chunk.y = packet.y;
		chunk.tiles = new Tile[chunksize][chunksize];
		for(int x = 0;x < chunksize;x ++){
			for(int y = 0;y < chunksize;y ++){
				int wx = packet.x + x;
				int wy = packet.y + y;
				if(wx < 0 || wy < 0 || wx >= worldwidth || wy >= worldheight) continue;
				chunk.tile(x,y) = tiles[wx][wy];
			}
		}
		return chunk;
		*/
	}

	public static World instance(){
		return KoruUpdater.instance.world;
	}

	public World(KoruServer server, boolean kek){
		this(null);
		this.server = server;
	}
	
	public Tile getTile(Point point){
		return tile(point.x, point.y);
	}

	public Tile getTile(float fx, float fy){
		int x = tile(fx);
		int y = tile(fy);
		//if(!bounds(x,y)) return null;
		return tile(x,y);
	}
	
	public Point findEmptySpace(int x, int y){
		//Structure structure = entity.groupc().structure;
	//	structure.getBuildState(x, y);
		for(int k = -1; k < 3; k ++){
			int i = (k+4)%4;
			int sx =DirectionUtils.toX(i), sy = DirectionUtils.toY(i);
			if(!blockSolid(x + sx, y + sy) /*&& structure.getBuildState(sx + x, sy + y) == BuildState.completed*/){
				point.set(x+sx, y+sy);
				return point;
			}
		}
	//	Koru.log("Error: empty point not found!");
		return null;
	}

	public boolean positionSolid(float x, float y){
		Tile tile = getTile(x,y);
		return (tile.block.getType().solid() && tile.block.getType().getRect(tile(x), tile(y), rect).contains(x, y))
				|| (tile.tile.getType().solid() && tile.tile.getType().getRect(tile(x), tile(y), rect).contains(x, y));
	}
	
	public boolean blockSolid(int x, int y){
		return tile(x,y).solid();
	}
	
	public boolean isAccesible(int x, int y){
		return !blockSolid(x-1,y) || !blockSolid(x+1,y) || !blockSolid(x,y-1) || !blockSolid(x,y+1);
	}

	public boolean blends(int x, int y, Material material){
		return !isType(x, y + 1, material) || !isType(x, y - 1, material) || !isType(x + 1, y, material) || !isType(x - 1, y, material);
	}

	public boolean isType(int x, int y, Material material){
		return tile(x,y).tile == material;
	}
	
	public Point search(Material material, int x, int y, int range){
		float nearest = Float.MAX_VALUE;
		for(int cx = -range; cx <= range; cx ++){
			for(int cy = -range; cy <= range; cy ++){
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
		if(nearest > 0)
			return point;
		
		return null;
	}

	public void updateTile(int x, int y){
		updated = true;
		tile(x, y).changeEvent();
		AIData.updateNode(x, y);
		server.sendToAll(new TileUpdatePacket(x, y, tile(x,y)));
	}
	
	
	protected Chunk generateChunk(int chunkx, int chunky){
		Chunk chunk = Pools.obtain(Chunk.class);
		chunk.set(chunkx, chunky);
		generator.generateChunk(chunk);
		loadedchunks.put(hashCoords(chunkx,chunky), chunk);
		return chunk;
	}
	
	protected void unloadChunk(Chunk chunk){
		file.writeChunk(chunk);
		loadedchunks.remove(hashCoords(chunk.x, chunk.y));
	}
	
	public Chunk getChunk(int chunkx, int chunky){
		Chunk chunk = loadedchunks.get(hashCoords(chunkx,chunky));
		if(chunk == null){
			if(file.chunkIsSaved(chunkx, chunky)){
				Chunk schunk = file.readChunk(chunkx, chunky);
				loadedchunks.put(hashCoords(chunkx,chunky), schunk);
				return schunk;
			}else{
				return generateChunk(chunkx, chunky);
			}
		}
		return chunk;
	}
	
	public Tile tile(int x, int y){
		int cx = x/chunksize, cy = y/chunksize;
		return getChunk(cx, cy).getWorldTile(x, y);
	}
	
	public int toChunkCoords(int a){
		return (a / chunksize);
	}
	
	public int toChunkCoords(float worldpos){
		return tile(worldpos) / chunksize;
	}
	
    public int hashCoords(int a, int b){
        return (a + b) * (a + b + 1) / 2 + a;
    }
	
	public boolean updated(){
		return updated;
	}

	public static int tile(float i){
		return (int)(i / tilesize);
	}
	
	public static float world(int i){
		return tilesize*i + tilesize/2;
	}
	public void init(){
		network = getModule(Network.class);
	}
}
