package io.anuke.koru;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.IntIntMap;

import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.BlockMap;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Atlas;

public class MapPreview extends ApplicationAdapter{
	TerrainGenerator gen;
	BlockMap<ChunkPix> blocks = new BlockMap<ChunkPix>();
	IntIntMap colors = new IntIntMap();
	SpriteBatch batch;
	int viewrange = 32;
	float scl = 0.1f;
	float vx, vy;
	float speed = 32;
	int pixsize = 128
			;
	int percision = 16;
	static double maxe;
	static double maxt;

	public void create(){
		UCore.maximizeWindow();
		batch = new SpriteBatch();
		gen = new TerrainGenerator();

		Atlas atlas = new Atlas(Gdx.files.absolute("/home/anuke/Projects/Koru/core/assets/sprites/koru.atlas"));

		Pixmap pixmap = atlas.getPixmapOf(atlas.findRegion("grass"));

		for(Material material : Materials.values()){
			if(material.getType() != MaterialType.tile && material.getType() != MaterialType.water)
				continue;
			AtlasRegion region = atlas.findRegion(material.name());
			colors.put(material.id(), pixmap.getPixel(region.getRegionX(), region.getRegionY()));
		}

		for(int x = -viewrange; x <= viewrange; x++){
			for(int y = -viewrange; y <= viewrange; y++){
				gen(x, y);
			}
		}
	}

	public void render(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();

		float speed = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? this.speed * 5 : this.speed;

		if(Gdx.input.isKeyPressed(Keys.W))
			vy += speed;
		if(Gdx.input.isKeyPressed(Keys.A))
			vx -= speed;
		if(Gdx.input.isKeyPressed(Keys.S))
			vy -= speed;
		if(Gdx.input.isKeyPressed(Keys.D))
			vx += speed;

		UCore.clearScreen(Color.BLACK);
		batch.begin();

		int viewx = (int) (vx / pixsize);
		int viewy = (int) (vy / pixsize);
		for(int x = -viewrange + viewx; x <= viewrange + viewx; x++){
			for(int y = -viewrange + viewy; y <= viewrange + viewy; y++){
				ChunkPix pix = blocks.get(x, y);
				if(pix != null){
					batch.draw(pix.texture, x * pixsize * scl + Gdx.graphics.getWidth() / 2 - vx*scl,
							y * pixsize * scl + Gdx.graphics.getHeight() / 2 - vy*scl, pixsize * scl, pixsize * scl);
				}else{
					gen(x, y);
				}
			}
		}
		batch.end();
	}

	void gen(int x, int y){
		blocks.put(x, y, new ChunkPix(x, y));
	}
	
	int getPix(int x, int y){
		//float temp = gen.getElevation(x, y);
		
		//return Color.rgba8888((Hue.blend(Color.BLACK, Color.WHITE, (float)temp)));
		//return /*Color.rgba8888(Hue.blend(Hue.blend(Color.FOREST, Color.TAN, (float)temp/5f), Hue.blend(Color.DARK_GRAY, Color.WHITE, (float)sum/5f), 0.5f));*/Color.rgba8888(Hue.blend2d(Color.FOREST, Color.GREEN, Color.TAN, Color.DARK_GRAY, (float)temp/5f, 
			//	(float)sum/5f));//Color.rgba8888(Hue.blend(Color.BLUE, Color.RED, (float)temp/5f));//colors.get(gen.generate(x, y).tileid, 0);
		Tile tile = gen.generate(x, y);
		if(!tile.blockEmpty()) return colors.get(tile.tileid, 0)+1000;
		return colors.get(tile.tileid, 0);
	}

	class ChunkPix{
		Pixmap pixmap;
		Texture texture;

		public ChunkPix(int x, int y) {
			pixmap = new Pixmap(pixsize/percision, pixsize/percision, Format.RGBA8888);
			
			for(int px = 0; px < pixmap.getWidth(); px++){
				for(int py = 0; py < pixmap.getHeight(); py++){

					
					pixmap.drawPixel(px, (pixmap.getHeight() - 1 - py), getPix(x * pixsize + px * percision, y * pixsize + py * percision));
				}
			}
			texture = new Texture(pixmap);
		}
	}

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}
