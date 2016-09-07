package net.pixelstatic.koru.network;

import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.systems.KoruEngine;
import net.pixelstatic.koru.world.World;

public abstract class IServer{
	private static IServer instance;
	
	{instance = this;}
	
	public static IServer instance(){
		return instance;
	}
	
	public static boolean active(){
		return instance != null;
	}
	
	abstract public void sendToAll(Object object);
	abstract public void sendTCP(int id, Object object);
	abstract public void sendUDP(int id, Object object);
	abstract public void sendEntity(KoruEntity entity);
	abstract public void removeEntity(KoruEntity entity);
	
	abstract public long getFrameID();
	abstract public KoruEngine getEngine();
	abstract public World getWorld();
}
