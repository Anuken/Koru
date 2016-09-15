package io.anuke.koru.network;

import io.anuke.koru.network.packets.ChunkPacket;
import io.anuke.koru.world.*;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class Serializer{
	static Json json = new Json();
	//static Kryo kryo = new Kryo();
	
	static{
		//Registrator.register(kryo);
	}
	
	public static String serialize(Object object){
		if(object instanceof ChunkPacket){
			ChunkPacket packet = (ChunkPacket)object;
			String string = "chunkpacket";
			string += "{"+packet.chunk.x + "," + packet.chunk.y+"}";
			for(int x = 0; x < World.chunksize; x ++){
				for(int y = 0; y < World.chunksize; y ++){
					string += (char)(packet.chunk.tiles[x][y].tile.id());
					string += (char)(packet.chunk.tiles[x][y].block.id());
				//	Koru.log(string.substring(string.length()-2));
				}
			}
			return string;
		}
		return "`"+object.getClass().getName() + "`"+ json.toJson(object);
	}
	
	public static Object deserialize(String message){
		if(message.startsWith("chunkpacket")){
			ChunkPacket packet = new ChunkPacket();
			packet.chunk = new Chunk();
			String chunk = message.replace("chunkpacket", "");
			String coords = chunk.substring(chunk.lastIndexOf('{')+1, chunk.lastIndexOf('}'));
			String[] split = coords.split(",");
			packet.chunk.x = Integer.parseInt(split[0]);
			packet.chunk.y = Integer.parseInt(split[1]);
			chunk = chunk.substring(chunk.lastIndexOf('}')+1);
			for(int i = 0; i < World.chunksize*World.chunksize; i ++){
				int tile = (int)((chunk.charAt(i*2)));
				int block = (int)((chunk.charAt(i*2+1)));
				Tile tiled = new Tile(Materials.values()[tile], Materials.values()[block]);
				packet.chunk.tiles[i % World.chunksize][i / World.chunksize] = tiled;
			}
			return packet;
		}
		String name = message.substring(1, message.lastIndexOf('`'));
		String obj = message.substring(message.lastIndexOf('`') + 1, message.length());
		
		Class<?> c = null;
		try{
			c = ClassReflection.forName(name);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return json.fromJson(c, obj);
	}
}
