package io.anuke.koru.world;

import com.badlogic.gdx.utils.Array;

import io.anuke.koru.Koru;
import io.anuke.koru.network.Net;
import io.anuke.koru.network.packets.ChunkPacket;
import io.anuke.koru.world.materials.Material;

public abstract class StructureGenerator{
	boolean locked = false;
	Array<Chunk> updated = new Array<>();
	Chunk current = null;
	
	public void generateChunk(Chunk chunk){
		if(locked){
			return;
		}
		
		locked = true;
		updated.clear();
		updated.add(chunk);
		
		this.current = chunk;
		generate(chunk);
		
		for(Chunk send : updated){
			ChunkPacket packet = new ChunkPacket();
			packet.chunk = send;
			Net.send(packet);
		}
		
		locked = false;
	}
	
	public void setTile(int x, int y, Material floor, Material wall){
		Tile tile = Koru.world.getTile(x, y);
		tile.setWall(wall);
		if(tile.topFloor() != floor) tile.addFloor(floor);
		
		Chunk chunk = Koru.world.getChunkFor(tile.x, tile.y);
		if(!updated.contains(chunk, true)){
			updated.add(chunk);
		}
	}
	
	protected abstract void generate(Chunk chunk);
}
