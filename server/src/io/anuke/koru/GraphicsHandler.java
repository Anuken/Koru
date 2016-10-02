package io.anuke.koru;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;

import io.anuke.fluxe.generation.Crux;
import io.anuke.fluxe.generation.Fluxor;
import io.anuke.koru.network.BitmapData;
import io.anuke.koru.network.packets.SplitBitmapPacket;
import io.anuke.ucore.UCore;

public class GraphicsHandler extends ApplicationAdapter{
	private static int nextBitmapID;
	final int maxImagePacketSize = 256;
	Crux crux;
	Fluxor flux;
	
	public Array<Object> generateBitmapPacketList(Pixmap pixmap){
		Array<Object> packets = new Array<Object>();
		BitmapData data = new BitmapData(pixmap);
		
		SplitBitmapPacket.Header header = new SplitBitmapPacket.Header();
		header.colors = data.colors;
		header.width = data.width;
		header.height = data.height;
		header.id = nextBitmapID++;
		packets.add(header);
		
		for(int i = 0; i < data.data.length/maxImagePacketSize; i++){
			SplitBitmapPacket packet = new SplitBitmapPacket();
			int len = Math.min(maxImagePacketSize, data.data.length-i*maxImagePacketSize);
			byte[] bytes = new byte[len];
			System.arraycopy(data.data, i*maxImagePacketSize, bytes, 0, len);
			packet.data = bytes;
			packet.id = header.id;
			packets.add(packet);
		}
		
		return packets;
	}

	public void create(){
		GLFW.glfwHideWindow(UCore.getWindowHandle());
		Gdx.graphics.setContinuousRendering(false);
	}

	public void render(){
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
		
		if(Gdx.input.isKeyJustPressed(Keys.E)){
			Pixmap pixmap = crux.render(flux);
			PixmapIO.writePNG(Gdx.files.local("tree.png"), pixmap);
		}
	}
	
	public Pixmap generate(){
		return crux.render(flux);
	}
}
