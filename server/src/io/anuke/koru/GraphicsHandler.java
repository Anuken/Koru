package io.anuke.koru;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;

import io.anuke.fluxe.generation.Crux;
import io.anuke.fluxe.generation.DefaultRasterizer;
import io.anuke.fluxe.generation.Fluxor;
import io.anuke.fluxe.generation.TreeVoxelizer;
import io.anuke.koru.generation.GeneratedMaterial;
import io.anuke.koru.generation.GeneratedObject;
import io.anuke.koru.generation.MaterialManager;
import io.anuke.koru.network.BitmapData;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.ObjectPiecePacket;
import io.anuke.koru.utils.Text;
import io.anuke.koru.world.MaterialType;
import io.anuke.ucore.UCore;

public class GraphicsHandler extends ApplicationAdapter{
	private static int nextBitmapID;
	final int maxImagePacketSize = 512;
	private int lastMaterialID;
	Crux crux;
	Fluxor flux;
	
	public void sendMaterial(GeneratedMaterial mat){
		Pixmap pix = mat.getPixmap();
	}
	
	public void generateNewMaterial(MaterialType type, Object... params){
		GeneratedMaterial mat = new GeneratedMaterial(type, ++lastMaterialID);
		MaterialManager.instance().registerMaterial(mat);
		Pixmap pix = generatePixmap(mat, params);
		mat.loadPixmap(pix);
		PixmapIO.writePNG(mat.getImageFile(), pix);
	}
	
	public Pixmap generatePixmap(GeneratedMaterial mat, Object... args){
		Pixmap p = crux.render(flux);
		return p;
	}
	
	public void sendPixmap(Pixmap pix, GeneratedObject object, int id){
		Gdx.app.postRunnable(()->{
			Array<Object> packets = generateBitmapPacketList(pix);
			Koru.log("Sending " + Text.LIGHT_MAGENTA + packets.size + Text.LIGHT_GREEN+ " split bitmap packets to " + id + ".");
			for(Object o : packets)
				IServer.instance().sendTCP(id, o);
		});
	}
	
	public Array<Object> generateBitmapPacketList(Pixmap pixmap){
		Array<Object> packets = new Array<Object>();
		BitmapData data = new BitmapData(pixmap);
		
		ObjectPiecePacket.Header header = new ObjectPiecePacket.Header();
		header.colors = data.colors;
		header.width = data.width;
		header.height = data.height;
		header.id = nextBitmapID++;
		packets.add(header);
		
		for(int i = 0; i < data.data.length/maxImagePacketSize+1; i++){
			ObjectPiecePacket packet = new ObjectPiecePacket();
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
		
		crux = new Crux();
		flux = new Fluxor(new TreeVoxelizer(), new DefaultRasterizer());
	}

	public void render(){
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
		
		if(Gdx.input.isKeyJustPressed(Keys.E)){
			Pixmap pixmap = crux.render(flux);
			PixmapIO.writePNG(Gdx.files.local("tree.png"), pixmap);
		}
	}
	
	public Pixmap generate(){
		Pixmap p = crux.render(flux);
		PixmapIO.writePNG(Gdx.files.local("tree.png"), p);
		return p;
	}
}
