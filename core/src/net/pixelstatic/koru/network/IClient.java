package net.pixelstatic.koru.network;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;

public abstract class IClient{
	public abstract Kryo getKryo();
	public abstract void sendTCP(Object object);
	public abstract void sendUDP(Object object);
	public abstract void connect(String ip, int port) throws IOException;
	public abstract void addListener(NetworkListener listener);
}
