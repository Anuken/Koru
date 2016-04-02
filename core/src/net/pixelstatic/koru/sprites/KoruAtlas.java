package net.pixelstatic.koru.sprites;

import java.util.HashMap;

import net.pixelstatic.koru.Koru;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//utility class that improves on TextureAtlas - faster texture lookups, and automatic error textures.
public class KoruAtlas extends TextureAtlas{
	HashMap<String, AtlasRegion> regionmap = new HashMap<String, AtlasRegion>();
	HashMap<Texture, Pixmap> pixmaps = new HashMap<Texture, Pixmap>();
	AtlasRegion error;
	
	public KoruAtlas(FileHandle file){
		super(file);
		for(AtlasRegion r : super.getRegions()){
			String[] split = r.name.split("/");
			if(split.length > 1){
				if(regionmap.containsKey(split[1])) Koru.log("--- WARNING: TEXTURE CONFLICT! --- " + "(" + split[1] + ")");
				regionmap.put(split[1], r);
			}else{
				if(regionmap.containsKey(split[0])) Koru.log("--- WARNING: TEXTURE CONFLICT! --- " + "(" + split[0] + ")");
				regionmap.put(split[0], r);
			}
			r.name = new String(r.name);
		}
		error = findRegion("error");
	}
	
	public Pixmap getPixmapOf(TextureRegion region){
		Texture texture = region.getTexture();
		if(pixmaps.containsKey(texture)) return pixmaps.get(texture);
		texture.getTextureData().prepare();
		Pixmap pixmap = texture.getTextureData().consumePixmap();
		pixmaps.put(texture, pixmap);
		return pixmap;
	}

	//returns error texture if region not found
	@Override
	public AtlasRegion findRegion(String name){
		AtlasRegion r = regionmap.get(name);
		if(r == null) return error;
		return r;
	}

	public float RegionHeight(String name){
		return findRegion(name).getRegionHeight();
	}

	public float RegionWidth(String name){
		return findRegion(name).getRegionWidth();
	}
	//if a texture is in the atlas
	public boolean IsBlank(String s){
		return findRegion(s).equals(error);
	}
}
