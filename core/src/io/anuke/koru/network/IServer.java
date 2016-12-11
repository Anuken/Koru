package io.anuke.koru.network;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.modules.World;
import io.anuke.koru.systems.KoruEngine;

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
	abstract public void sendToAllExcept(int id, Object object);
	abstract public void sendTCP(int id, Object object);
	abstract public void sendUDP(int id, Object object);
	abstract public void sendEntity(KoruEntity entity);
	abstract public void sendLater(Object object);
	abstract public void removeEntity(KoruEntity entity);
	
	abstract public long getFrameID();
	abstract public float getDelta();
	abstract public KoruEngine getEngine();
	abstract public World getWorld();
}
