package net.pixelstatic.koruserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.network.IServer;
import net.pixelstatic.koru.world.*;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.compression.Lzma;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class WorldFile extends WorldLoader{
	public final String filename = "chunk", extension = ".kw";
	private Path file;
	private ObjectMap<String, Path> files = new ObjectMap<String, Path>();
	private Kryo kryo;
	private ConcurrentHashMap<Long, Chunk> loadedchunks = new ConcurrentHashMap<Long, Chunk>(); //server-side chunks
	private Generator generator;
	private Object lock = new Object();

	public WorldFile(Path file, Generator generator){

		if( !Files.isDirectory(file)) throw new RuntimeException("World file has to be a directory!");

		kryo = new Kryo();
		kryo.register(Chunk.class);
		kryo.register(Materials.class);
		this.file = file;
		this.generator = generator;

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

		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				if( !IServer.active()) return;
				Koru.log("Saving " + loadedchunks.size() + " chunks...");
				for(Chunk chunk : loadedchunks.values()){
					writeChunk(chunk);
				}
			}
		});

		if(totalChunks() != 0) return;
		generateChunk(0, 0);
		generateChunk( -1, 0);
		generateChunk(0, -1);
		generateChunk( -1, -1);

	}

	public boolean chunkIsSaved(int x, int y){
		return getPath(x, y) != null;
	}

	public void writeChunk(Chunk chunk){
		synchronized (lock){
			Koru.log(Consolec.RED + "BEGIN" + Consolec.BLUE + " write chunk" + Consolec.RESET);
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

			Koru.log(Consolec.GREEN + "END" + Consolec.BLUE + " write chunk" + Consolec.RESET);
		}
	}

	public Chunk readChunk(int x, int y){
		synchronized (lock){
			Koru.log(Consolec.RED + "BEGIN" + Consolec.YELLOW + " read chunk" + Consolec.RESET);
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

				Koru.log(Consolec.GREEN + "END" + Consolec.YELLOW + " read chunk" + Consolec.RESET);
				return chunk;
			}catch(Exception e){
				Koru.log("Error writing chunk!");
				e.printStackTrace();
			}
			throw new RuntimeException("Error reading chunk!");
		}
	}

	public Chunk generateChunk(int chunkx, int chunky){
		Chunk chunk = Pools.obtain(Chunk.class);
		chunk.set(chunkx, chunky);
		generator.generateChunk(chunk);
		loadedchunks.put(hashCoords(chunkx, chunky), chunk);

		//file.writeChunk(chunk);
		return chunk;
	}

	@Override
	public void unloadChunk(Chunk chunk){
		writeChunk(chunk);
		loadedchunks.remove(hashCoords(chunk.x, chunk.y));
	}

	@Override
	public Chunk getChunk(int chunkx, int chunky){
		Chunk chunk = loadedchunks.get(hashCoords(chunkx, chunky));
		if(chunk == null){
			if(chunkIsSaved(chunkx, chunky)){
				Chunk schunk = readChunk(chunkx, chunky);
				loadedchunks.put(hashCoords(chunkx, chunky), schunk);
				return schunk;
			}else{
				return generateChunk(chunkx, chunky);
			}
		}
		return chunk;
	}

	public Collection<Chunk> getLoadedChunks(){
		return loadedchunks.values();
	}

	public String fileName(int x, int y){
		return filename + hashCoords(x, y) + extension;
	}

	public int totalChunks(){
		return files.size;
	}

	private Path getPath(int x, int y){
		return files.get(fileName(x, y));
	}

	public static long hashCoords(int a, int b){
		return (((long)a) << 32) | (b & 0xffffffffL);
	}
}
