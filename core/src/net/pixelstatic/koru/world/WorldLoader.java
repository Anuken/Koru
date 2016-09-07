package net.pixelstatic.koru.world;

import java.nio.file.Path;

public abstract class WorldLoader{
	protected final Path file;
	
	public WorldLoader(Path file){
		this.file = file;
	}
	
	abstract public boolean chunkIsSaved(int x, int y);
	abstract public void writeChunk(Chunk chunk);
	abstract public Chunk readChunk(int x, int y);
	abstract public int totalChunks();
}
