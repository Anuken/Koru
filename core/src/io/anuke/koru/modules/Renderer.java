package io.anuke.koru.modules;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.renderers.ParticleRenderer;
import io.anuke.koru.utils.RepackableAtlas;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.InventoryTileData;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.World;
import io.anuke.layer3d.LayerList;
import io.anuke.layer3d.LayeredRenderer;
import io.anuke.ucore.UCore;
import io.anuke.ucore.g3d.Models;
import io.anuke.ucore.graphics.FrameBufferMap;
import io.anuke.ucore.graphics.Textures;
import io.anuke.ucore.modules.Module;

public class Renderer extends Module<Koru>{
	public static final int viewrangex = 28;
	public static final int viewrangey = 20;
	public static Renderer i;
	public final float GUIscale = 5f;
	public final int scale = 4;
	public World world;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public RepackableAtlas atlas;
	public GlyphLayout layout;
	public BitmapFont font;
	public FrameBufferMap buffers;
	public boolean debug = true;
	public KoruEntity player;
	public LayerList[][] renderables = new LayerList[World.chunksize * World.loadrange * 2][World.chunksize * World.loadrange * 2];
	public int lastcamx, lastcamy;

	public Renderer(){
		UCore.maximizeWindow();
		i = this;
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/scale, Gdx.graphics.getHeight()/scale);
		atlas = new RepackableAtlas(Gdx.files.internal("sprites/koru.atlas"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		layout = new GlyphLayout();
		buffers = new FrameBufferMap();
		LayeredRenderer.instance().camera = camera;
		Textures.load("");
	}

	public void init(){
		player = getModule(ClientData.class).player;
		world = getModule(World.class);
		ParticleRenderer.loadParticles(this);
	}

	@Override
	public void update(){
		updateCamera();
		updateMap();
		
		UCore.clearScreen(Color.SKY);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		LayeredRenderer.instance().render(batch);
		batch.end();
		
		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth()/GUIscale, Gdx.graphics.getHeight()/GUIscale);
		batch.begin();
		drawGUI();
		batch.end();
		batch.setColor(Color.WHITE);
		updateCamera();
	}
	
	void updateCamera(){
		LayeredRenderer.instance().baserotation = 45f;
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
	}

	void updateMap(){
		if(Gdx.graphics.getFrameId() == 5) updateTiles();
		int camx = Math.round(player.getX() / World.tilesize), camy = Math.round(player.getY() / World.tilesize);

		if(lastcamx != camx || lastcamy != camy){
		//	updateTiles();
		}

		lastcamx = camx;
		lastcamy = camy;
	}
	
	public void updateTiles(){
		updateTiles(0);
	}

	/**If resetID does not equal 0, it will only updated tiles whose ID is resetID*/
	public void updateTiles(int resetID){

		int camx = Math.round(player.getX() / World.tilesize), camy = Math.round(player.getY() / World.tilesize);

		for(int chunkx = 0;chunkx < World.loadrange * 2;chunkx ++){
			for(int chunky = 0;chunky < World.loadrange * 2;chunky ++){
				Chunk chunk = world.chunks[chunkx][chunky];
				if(chunk == null) continue;
				for(int x = 0;x < World.chunksize;x ++){
					for(int y = 0;y < World.chunksize;y ++){
						int worldx = chunk.worldX() + x;
						int worldy = chunk.worldY() + y;
						int rendx = chunkx * World.chunksize + x, rendy = chunky * World.chunksize + y;
						
						Tile tile = chunk.tiles[x][y];
						
						if(resetID != 0 && (tile.blockid != resetID && tile.tileid != resetID)) continue;
						
						if(renderables[rendx][rendy] != null) renderables[rendx][rendy].free();
						if(Math.abs(worldx - camx) > viewrangex || Math.abs(worldy - camy) > viewrangey) continue;
						
						
						if(renderables[rendx][rendy] != null){
							renderables[rendx][rendy].free();
						}else{
							renderables[rendx][rendy] = new LayerList();
						}

						if(!tile.tileEmpty()){
							tile.tile().getType().draw(renderables[rendx][rendy], tile.tile(), tile, worldx, worldy);
						}

						if(!tile.blockEmpty()){
							tile.block().getType().draw(renderables[rendx][rendy], tile.block(), tile, worldx, worldy);
						}
					}
				}
			}
		}
	}
	

	public void drawGUI(){
		font.getData().setScale(1 / GUIscale);
		font.setColor(Color.WHITE);

		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS", 0, uiheight());
		
		String launcher = "";
		
		if(Gdx.app.getType() != ApplicationType.WebGL){
			launcher = System.getProperty("sun.java.command");
			launcher = launcher.substring(launcher.lastIndexOf(".") + 1, launcher.length()).replace("Launcher", "");
		}else{
			launcher = "GWT";
		}
		
		layout.setText(font, launcher);
		
		font.setColor(Color.CORAL);
		font.draw(batch, launcher, gwidth()/2 - layout.width/2, gheight());
		
		font.setColor(Color.WHITE);

		if(debug){
			GridPoint2 cursor = getModule(Input.class).cursorblock();
			float cx = Gdx.input.getX() / GUIscale, cy = Gdx.graphics.getHeight() / GUIscale - Gdx.input.getY() / GUIscale;
			if( !world.inBounds(cursor.x, cursor.y)){
				font.draw(batch, "Out of bounds.", cx, cy);

				return;
			}
			Tile tile = world.getTile(cursor);


			Chunk chunk = world.getRelativeChunk(cursor.x, cursor.y);
			font.draw(batch, cursor.x + ", " + cursor.y + " " + tile + " chunk: " + chunk.x + "," + chunk.y + "\nchunk block pos: " + (cursor.x - chunk.worldX()) + ", " + (cursor.y - chunk.worldY()) + "\n" + "chunk pos: " + chunk.x + ", " + chunk.y, cx, cy);

			if(tile.blockdata instanceof InventoryTileData){
				InventoryTileData data = tile.getBlockData(InventoryTileData.class);
				font.draw(batch, data.inventory.toString(), cx, cy);
			}
		}

	}

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width / GUIscale, height / GUIscale);
		camera.setToOrtho(false, width/scale, height/scale);
		camera.update();
	}

	public GlyphLayout getBounds(String text){
		layout.setText(font, text);
		return layout;
	}

	//returns screen width / scale
	public float uiwidth(){
		return Gdx.graphics.getWidth() / GUIscale;
	}

	//returns screen height / scale
	public float uiheight(){
		return Gdx.graphics.getHeight() / GUIscale;
	}

	public TextureAtlas atlas(){
		return atlas;
	}

	public TextureRegion getRegion(String name){
		return atlas.findRegion(name);
	}


	public SpriteBatch batch(){
		return batch;
	}

	public BitmapFont font(){
		return font;
	}
	
	public void dispose(){
		Models.dispose();
	}
}
