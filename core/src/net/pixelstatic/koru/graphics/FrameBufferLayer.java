package net.pixelstatic.koru.graphics;

public abstract class FrameBufferLayer{
	String name;
	float layer;
	
	abstract void end();
	abstract void begin();
}
