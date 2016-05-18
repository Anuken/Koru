package net.pixelstatic.koru.world;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import net.pixelstatic.koru.Koru;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.compression.Lzma;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class WorldFile{
	public final String filename = "chunk", extension = ".kw";
	private Path file;
	private ObjectMap<String, Path> files = new ObjectMap<String, Path>();
	private Kryo kryo;
	private ByteBuffer buffer = ByteBuffer.allocate(4);

	public WorldFile(Path file){
		if( !Files.isDirectory(file)) throw new RuntimeException("World file has to be a directory!");

		kryo = new Kryo();
		kryo.register(Chunk.class);
		this.file = file;

		try{
			Stream<Path> stream = Files.list(file);

			stream.forEach((Path path) -> {
				if(path.toString().endsWith(extension)) files.put(path.getFileName().toString(), path);
			});

			stream.close();
		}catch(Exception e){
			e.printStackTrace();
			System.exit( -1);
		}
		if(files.size > 0){
			Koru.log("Found " + files.size + " world chunk" + (files.size == 1 ? "" : "s") + ".");
		}else{
			Koru.log("Found empty world.");
		}
	}

	public boolean chunkIsSaved(int x, int y){
		return getPath(x, y) != null;
	}

	public void writeChunk(Chunk chunk){
		Path path = Paths.get(file.toString(), "/" + fileName(chunk.x, chunk.y));
		
		long time = TimeUtils.millis();
		
		try{
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
	
			Koru.log("Chunk write time elapsed: " + TimeUtils.timeSinceMillis(time));
		}catch(Exception e){
			Koru.log("Error writing chunk!");
			e.printStackTrace();
		}
		files.put(path.getFileName().toString(), path);
	}

	public Chunk readChunk(int x, int y){
		Path path = getPath(x, y);

		long time = TimeUtils.millis();
		
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			FileInputStream file = new FileInputStream(path.toFile());
			
			Lzma.decompress(file, out);		
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			
			Input input = new Input(in);
			Chunk chunk = kryo.readObject(input, Chunk.class);
			
			input.close();
			out.close();
			in.close();
			file.close();
			
			Koru.log("Chunk read time elapsed: " + TimeUtils.timeSinceMillis(time));
			
			return chunk;
		}catch(Exception e){
			Koru.log("Error writing chunk!");
			e.printStackTrace();
		}
		return null;
	}

	public String fileName(int x, int y){
		return filename + World.hashCoords(x, y) + extension;
	}

	public int totalChunks(){
		return files.size;
	}
	
	private byte[] toBytes(Chunk chunk){
		byte[] bytes = new byte[World.tilesize * World.tilesize * 4*2];
		int i = 0;
		for(int x = 0; x < chunk.tiles.length; x ++){
			for(int y = 0; y < chunk.tiles.length; y ++){
				buffer.clear();
				Tile tile = chunk.tiles[x][y];
				buffer.putInt(tile.block.ordinal());
				for(int g = 0; g < 4; g ++){
					bytes[i] = buffer.array()[g];
					i ++;
				}
			}
		}
		return bytes;
	}

	private Path getPath(int x, int y){
		return files.get(fileName(x, y));
	}
}
