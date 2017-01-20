package io.anuke.koru.server.world;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;

import com.badlogic.gdx.utils.compression.Lzma;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import io.anuke.koru.Koru;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.Materials;

public class ChunkWriter{
	private Kryo kryo;
	private boolean writing;
	
	public ChunkWriter(){
		kryo = new Kryo();
		kryo.register(Chunk.class);
		kryo.register(Materials.class);
	}

	public void writeChunk(Chunk chunk, Path path, boolean compress){
		writing = true;
		try{
			if(compress){
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Output output = new Output(stream);
				kryo.writeObject(output, chunk);
				output.close();
				
				ByteArrayInputStream in = new ByteArrayInputStream(stream.toByteArray());
				FileOutputStream file = new FileOutputStream(path.toString());
				
				Lzma.compress(in, file);
				
				stream.close();
				in.close();
				file.close();
			}else{
				FileOutputStream file = new FileOutputStream(path.toString());
				Output output = new Output(file);
				kryo.writeObject(output, chunk);
				output.close();
			}

		}catch(Exception e){
			Koru.log("Error writing chunk!");
			e.printStackTrace();
		}
		writing = false;
	}
	
	public boolean writing(){
		return writing;
	}
}
