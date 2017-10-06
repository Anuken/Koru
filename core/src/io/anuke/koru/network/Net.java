package io.anuke.koru.network;

import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryonet.FrameworkMessage;

import io.anuke.koru.Koru;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.function.Consumer;

//TODO implement everything
public class Net{
	private static boolean server;
	private static NetProvider provider;
	private static ObjectMap<Class<?>, Consumer<Object>> packetHandlers = new ObjectMap<>();
	
	public static void setProvider(boolean server, NetProvider provider){
		Net.server = server;
		Net.provider = provider;
	}
	
	public static boolean server(){
		return server;
	}
	
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
		provider.send(object, mode);
	}
	
	public static void sendRange(Object object, float x, float y, float range, Mode mode){
		provider.sendRange(object, x, y, range, mode);
	}
	
	public static void sendTo(int clientid, Object object, Mode mode){
		provider.sendTo(clientid, object, mode);
	}
	
	public static void sendExcept(int clientid, Object object, Mode mode){
		provider.sendExcept(clientid, object, mode);
	}
	
	public static void sendSpark(Spark spark){
		provider.sendSpark(spark);
	}
	
	public static void removeSpark(Spark spark){
		provider.removeSpark(spark);
	}
	
	public static <T> void handle(Class<T> type, Consumer<T> handler){
		packetHandlers.put(type, (Consumer<Object>) handler);
	}
	
	public static void onRecieve(Object object){
		if(packetHandlers.containsKey(object.getClass())){
			packetHandlers.get(object.getClass()).accept(object);
		}else if(!(object instanceof FrameworkMessage)){
			Koru.log("Unhandled packet type: " + object.getClass().getSimpleName());
		}
	}
	
	public static enum Mode{
		TCP, UDP;
	}
	
	public static interface NetProvider{
		
		public void send(Object object, Mode mode);
		
		public void sendRange(Object object, float x, float y, float range, Mode mode);
		
		public void sendTo(int clientid, Object object, Mode mode);
		
		public void sendExcept(int clientid, Object object, Mode mode);
		
		public void sendSpark(Spark spark);
		
		public void removeSpark(Spark spark);
	}
}
