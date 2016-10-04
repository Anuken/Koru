package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

import io.anuke.koru.Koru;
import io.anuke.koru.network.BitmapData;
import io.anuke.koru.network.packets.GeneratedMaterialPacket;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.modules.Module;

public class ObjectHandler extends Module<Koru>{
	private final FileHandle objectDirectory = Gdx.files.local("objects");
	
	public void materialPacketRecieved(GeneratedMaterialPacket packet){
		
	}
	
	public void bitmapRecieved(BitmapData data){
		Pixmap pix = data.toPixmap();
		Texture texture = new Texture(pix);
		PixmapIO.writePNG(objectDirectory.child("recieved.png"), pix);
		
		Resources.getAtlas().addTexture("name", texture);
	}

}
