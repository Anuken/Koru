package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.Koru;
import io.anuke.koru.generation.GeneratedMaterial;
import io.anuke.koru.generation.MaterialManager;
import io.anuke.koru.network.BitmapData;
import io.anuke.koru.network.packets.GeneratedMaterialPacket;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.modules.Module;

public class ObjectHandler extends Module<Koru>{
	private ObjectMap<Integer, Pixmap> materials = new ObjectMap<>();
	
	public ObjectHandler(){
		MaterialManager.instance().loadMaterials(Gdx.files.local("materials.json"));
	}
	
	public void materialPacketRecieved(GeneratedMaterialPacket packet){
		GeneratedMaterial mat = packet.wrapper.asMaterial();
		Pixmap pix = materials.get(packet.bitmapID);
		MaterialManager.instance().registerMaterial(mat);
		//TODO save image on separate thread
		if(pix == null) throw new GdxRuntimeException("Error: Material sent but bitmap not found!");
		
		PixmapIO.writePNG(mat.getImageFile(), pix); 
		
		Texture texture = new Texture(pix);
		Resources.getAtlas().addTexture(mat.name(), texture);
	}
	
	public void bitmapRecieved(int id, BitmapData data){
		Pixmap pix = data.toPixmap();
		materials.put(id, pix);
	}
	
	@Override
	public void dispose(){
		MaterialManager.instance().saveMaterials(Gdx.files.local("materials.json"));
	}

}
