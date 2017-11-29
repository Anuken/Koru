package io.anuke.koru.world;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LongArray;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.World;
import io.anuke.koru.network.Net;
import io.anuke.koru.network.Net.Mode;
import io.anuke.koru.network.packets.BatchTileUpdatePacket;
import io.anuke.koru.world.materials.Material;

public abstract class StructureGenerator{
	boolean locked = false;
	LongArray coordinates = new LongArray();
	IntArray ids = new IntArray();
	Chunk current = null;
	
	public void generateChunk(Chunk chunk){
		if(locked){
			return;
		}
		
		locked = true;
		
		this.current = chunk;
		generate(chunk);
		
		if(coordinates.size > 0)
			sendPacket();
		
		locked = false;
	}
	
	public void setTile(int x, int y, Material floor, Material wall){
		Tile tile = Koru.world.getTile(x, y);
		tile.setWall(wall);
		if(tile.topFloor() != floor) tile.addFloor(floor);
		
		if(x >= current.worldX() && y >= current.worldY() &&
				x < current.worldX() + World.chunksize && y < current.worldY() + World.chunksize){
			return;
		}
		
		Koru.world.updateTile(tile);
		
		//TODO batching of positions?
		//ids.add(Bits.packInt((short)floor.id(), (short)wall.id()));
		//coordinates.add(Bits.packLong(x, y));
		
		//if(coordinates.size > 8*8){
		//	sendPacket();
		//}//
	}
	
	void sendPacket(){
		BatchTileUpdatePacket packet = new BatchTileUpdatePacket();
		packet.coords = coordinates.toArray();
		packet.ids = ids.toArray();
		
		Net.send(packet, Mode.TCP);
		
		coordinates.clear();
		ids.clear();
	}
	
	protected abstract void generate(Chunk chunk);
}
