package io.anuke.koru.network;

import io.anuke.koru.modules.World;
import io.anuke.ucore.ecs.Basis;
import io.anuke.ucore.ecs.Spark;

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
	abstract public void sendToAllIn(Object object, float x, float y, float range);
	
	abstract public void send(int id, Object object, boolean udp);
	
	abstract public void sendSpark(Spark spark);
	abstract public void removeSpark(Spark spark);
	
	abstract public Basis getBasis();
	abstract public World getWorld();
}
