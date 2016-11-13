package io.anuke.koru.world;

import java.util.Collection;

public abstract class WorldLoader{
	
	abstract public boolean chunkIsSaved(int x, int y);
	abstract public void unloadChunk(Chunk chunk);
	abstract public Chunk getChunk(int x, int y);
	abstract public Collection<Chunk> getLoadedChunks();
}
