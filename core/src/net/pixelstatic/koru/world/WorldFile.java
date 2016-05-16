package net.pixelstatic.koru.world;

import com.badlogic.gdx.files.FileHandle;

public class WorldFile{
	private FileHandle file;
	
	public WorldFile(FileHandle file){
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
