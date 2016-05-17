package net.pixelstatic.koru.world;

import java.io.File;


public class WorldFile{
	private File file;
	
	public WorldFile(File file){
		this.file = file;
	}
	
	public boolean chunkIsSaved(int x, int y){
		//TODO
		return false;
	}
	
	public void writeChunk(Chunk chunk){
		//TODO
	}
	
	public Chunk readChunk(int x, int y){
		//TODO
		return null;
	}
}
