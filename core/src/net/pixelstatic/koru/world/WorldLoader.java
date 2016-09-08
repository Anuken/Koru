package net.pixelstatic.koru.world;

import java.util.Collection;

public abstract class WorldLoader{
	
	abstract public boolean chunkIsSaved(int x, int y);
	//abstract public void writeChunk(Chunk chunk);
	//abstract public Chunk readChunk(int x, int y);
	
	//abstract public int totalChunks();
	abstract public void unloadChunk(Chunk chunk);
	abstract public Chunk getChunk(int x, int y);
	abstract public Collection<Chunk> getLoadedChunks();
}
