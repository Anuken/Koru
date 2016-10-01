package io.anuke.koru.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import io.anuke.koru.Koru;

public class KryoClient extends IClient{
	private Client client;
	private boolean started;
	
	{	
		int buffer = (int)Math.pow(2, 6)*8192;
		client = new Client(buffer, buffer);
		Registrator.register(client.getKryo());
	}

	@Override
	public void sendTCP(Object object){
		client.sendTCP(object);
	}

	@Override
	public void sendUDP(Object object){
		client.sendUDP(object);
	}

	@Override
	public void connect(String ip, int port) throws IOException{
		if(!started){
			Koru.log("starting");
			client.start();
			started = true;
		}
		client.connect(12000, ip, port, port);
	}

	@Override
	public void addListener(final NetworkListener listener){
		client.addListener(new Listener(){
			@Override
			public void received(Connection connection, Object object){
				listener.received(object);
			}
		});
	}

	@Override
	public boolean isConnected() {
		return client.isConnected();
	}

}
