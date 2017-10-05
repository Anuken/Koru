package io.anuke.koru.network;

import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.function.Consumer;

//TODO implement everything
public class Net{
	public static boolean server;
	
	public static void send(Object object){
		send(object, Mode.TCP);
	}
	
	public static void sendTo(int clientid, Object object){
		sendTo(clientid, object, Mode.TCP);
	}
	
	public static void sendExcept(int clientid, Object object){
		sendExcept(clientid, object, Mode.TCP);
	}
	
	public static void send(Object object, Mode mode){
		
	}
	
	public static void sendRange(Object object, Mode mode, float x, float y, float range){
		
	}
	
	public static void sendTo(int clientid, Object object, Mode mode){
		
	}
	
	public static void sendExcept(int clientid, Object object, Mode mode){
		
	}
	
	public static void sendSpark(Spark spark){
		
	}
	
	public static void removeSpark(Spark spark){
		
	}
	
	public static <T> void handle(Class<T> type, Consumer<T> handler){
		
	}
	
	public static enum Mode{
		TCP, UDP;
	}
}
