package net.pixelstatic.koru.modules;

import java.awt.Point;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.network.packets.ChunkPacket;
import net.pixelstatic.koru.network.packets.ChunkRequestPacket;
import net.pixelstatic.koru.network.packets.TileUpdatePacket;
import net.pixelstatic.koru.server.KoruServer;
import net.pixelstatic.koru.server.KoruUpdater;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.Tile;

public class World extends Module{
	public static final int chunksize = 10;
	public static final int loadrange = 2;
	public static final int tilesize = 12;
	public static final int worldwidth = 100, worldheight = 100;
	Network network;
	KoruServer server;
	public Tile[][] tiles;
	boolean[][] chunkloaded;

	@Override
	public void update(){
		sendChunkRequest();
	}

	public void loadChunks(ChunkPacket packet){
		for(int x = packet.x;x < packet.x + chunksize;x ++){
			for(int y = packet.y;y < packet.y + chunksize;y ++){
				tiles[x][y] = packet.tiles[x - packet.x][y - packet.y];
			}
		}
		chunkloaded[packet.x / chunksize][packet.y / chunksize] = true;
	}

	public void sendChunkRequest(){
		float px = getModule(Renderer.class).camera.position.x;
		float py = getModule(Renderer.class).camera.position.y;
		int blockx = (int)(px / tilesize), blocky = (int)(py / tilesize);
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
				*/
			}
		}
	}

	public ChunkPacket createChunkPacket(ChunkRequestPacket packet){
		ChunkPacket chunk = new ChunkPacket();
		chunk.x = packet.x;
		chunk.y = packet.y;
		chunk.tiles = new Tile[chunksize][chunksize];
		for(int x = 0;x < chunksize;x ++){
			for(int y = 0;y < chunksize;y ++){
				int wx = packet.x + x;
				int wy = packet.y + y;
				if(wx < 0 || wy < 0 || wx >= worldwidth || wy >= worldheight) continue;
				chunk.tiles[x][y] = tiles[wx][wy];
			}
		}
		return chunk;
	}
	
	public static World instance(){
		return KoruUpdater.instance.world;
	}

	public World(KoruServer server, boolean kek){
		this(null);
		this.server = server;
	}

	public World(Koru k){
		super(k);
		tiles = new Tile[worldwidth][worldheight];
		for(int x = 0;x < worldwidth;x ++){
			for(int y = 0;y < worldwidth;y ++){
				tiles[x][y] = new Tile();
			}
		}
		if( !KoruServer.active){
			chunkloaded = new boolean[worldwidth / chunksize][worldheight / chunksize];
		}
	}

	public World(){
		this(null);
	}

	public static boolean inBounds(int x, int y){
		return x >= 0 && y >= 0 && x < worldwidth && y < worldheight;
	}
	
	public Tile getTile(Point point){
		return tiles[point.x][point.y];
	}

	public boolean blends(int x, int y, Material material){
		return !isType(x, y + 1, material) || !isType(x, y - 1, material) || !isType(x + 1, y, material) || !isType(x - 1, y, material);
	}

	public boolean isType(int x, int y, Material material){
		return !inBounds(x, y) || tiles[x][y].tile == material;
	}

	public void updateTile(int x, int y){
		tiles[x][y].changeEvent();
		server.sendToAll(new TileUpdatePacket(x, y, tiles[x][y]));
	}

	public static int tile(float i){
		return (int)(i / tilesize);
	}

	public static int worldWidthPixels(){
		return tilesize * worldwidth;
	}

	public static int worldHeightPixels(){
		return tilesize * worldheight;
	}

	public void init(){
		network = getModule(Network.class);
	}
}
