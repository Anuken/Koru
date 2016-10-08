package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.Koru;
import io.anuke.koru.generation.GeneratedMaterial;
import io.anuke.koru.generation.MaterialManager;
import io.anuke.koru.network.BitmapData;
import io.anuke.koru.network.packets.GeneratedMaterialPacket;
import io.anuke.koru.network.packets.MaterialRequestPacket;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.modules.Module;

public class ObjectHandler extends Module<Koru>{
	private static ObjectHandler instance;
	private ObjectMap<Integer, Pixmap> materials = new ObjectMap<>();
	private IntSet loadingMaterials = new IntSet();
	
	public ObjectHandler(){
		instance = this;
		MaterialManager.instance().loadMaterials(Gdx.files.local("materials.json"));
	}
	
	public void notifyMaterialUnknown(int id){
		if(loadingMaterials.contains(id)) return;
		loadingMaterials.add(id);
		
		MaterialRequestPacket request = new MaterialRequestPacket();
		request.id = id;
		getModule(Network.class).client.sendTCP(request);
	}
	
	/**Called on the main GDX thread.*/
	public void materialPacketRecieved(GeneratedMaterialPacket packet){
		Koru.log("Recieved material packet.");
		GeneratedMaterial mat = packet.wrapper.asMaterial();
		Pixmap pix = materials.get(packet.bitmapID);
		MaterialManager.instance().registerMaterial(mat);
		//TODO save image on separate thread
		if(pix == null) throw new GdxRuntimeException("Error: Material sent but bitmap not found!");
		
		PixmapIO.writePNG(mat.getImageFile(), pix); 
		
		Texture texture = new Texture(pix);
		Resources.getAtlas().addTexture(mat.name(), texture);
		
		getModule(Renderer.class).updateTiles(mat.id());
	}
	
	public void bitmapRecieved(int id, BitmapData data){
		Koru.log("Accepted material bitmap.");
		Pixmap pix = data.toPixmap();
		materials.put(id, pix);
	}
	
	@Override
	public void dispose(){
		MaterialManager.instance().saveMaterials(Gdx.files.local("materials.json"));
	}
	
	public static ObjectHandler instance(){
		return instance;
	}

}
