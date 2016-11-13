package io.anuke.koru.network;

import java.io.IOException;

public abstract class IClient{
	public abstract void sendTCP(Object object);
	public abstract void sendUDP(Object object);
	public abstract void connect(String ip, int port) throws IOException;
	public abstract void addListener(NetworkListener listener);
	public abstract boolean isConnected();
}
