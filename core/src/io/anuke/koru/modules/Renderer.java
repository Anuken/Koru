package io.anuke.koru.modules;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.graphics.FrameBufferLayer;
import io.anuke.koru.renderers.ParticleRenderer;
import io.anuke.koru.utils.RepackableAtlas;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.InventoryTileData;
import io.anuke.koru.world.MaterialType;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.World;
import io.anuke.ucore.Noise;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.FrameBufferMap;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.spritesystem.LayerManager;
import io.anuke.ucore.spritesystem.Renderable;
import io.anuke.ucore.spritesystem.RenderableHandler;
import io.anuke.ucore.spritesystem.RenderableList;

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
	public Matrix4 matrix;
	public GlyphLayout layout;
	public BitmapFont font;
	public FrameBufferMap buffers;
	public boolean debug = true;
	public final boolean gbuffer = false;
	public KoruEntity player;
	public RenderableList[][] renderables = new RenderableList[World.chunksize * World.loadrange * 2][World.chunksize
			* World.loadrange * 2];
	public int lastcamx, lastcamy;

	public Renderer() {
		UCore.maximizeWindow();
		i = this;
		batch = new SpriteBatch();
		matrix = new Matrix4();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);
		atlas = new RepackableAtlas(Gdx.files.internal("sprites/koru.atlas"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		layout = new GlyphLayout();
		buffers = new FrameBufferMap();
		RenderableHandler.getInstance().setLayerManager(new LayerManager(){
			public void draw(Array<Renderable> renderables, Batch batch){
				drawRenderables(renderables);
			}
		});

		FrameBufferLayer.loadShaders();

		if(gbuffer)
			buffers.add("global", Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);

		setupTransitions();
	}

	void setupTransitions(){
		Pixmap pix = atlas.getPixmapOf(atlas.findRegion("grass"));
		Color color = new Color();
		for(Materials mat : Materials.values()){
			if(mat.getType() == MaterialType.tile && mat != Materials.stone){
				
				TextureRegion region = atlas.findRegion(mat.name() + "edge");
				TextureRegion orig = atlas.findRegion(mat.name());
				Pixmap.setBlending(Blending.None);
				
				
				for(int x = 0; x < region.getRegionWidth(); x++){
					for(int y = 0; y < region.getRegionHeight(); y++){
						pix.drawPixel(region.getRegionX() + x, region.getRegionY() + y, 0);
					}
				}
				
				Pixmap.setBlending(Blending.SourceOver);
				
				float alpha = 1f;
				for(int i = 0; i < 3; i++){
					int r = 6+i;
					for(int x = -r; x < r; x++){
						for(int y = -r; y < r; y++){
							
							color.set(pix.getPixel(orig.getRegionX() + (x+32) % 10, orig.getRegionY() + (y+32) % 10));
							color.a = alpha + Noise.normalNoise(x, y, 4, 0.3f) - 0.1f;
							color.clamp();
							color.mul(1.1f, 1.1f, 1.1f, 1f);
							if(i == 2 && Noise.normalNoise(x, y, 3, 1f) > 0.1f) color.a = 0.05f;
							pix.setColor(color);
							pix.drawPixel(region.getRegionX() + x + region.getRegionWidth()/2, region.getRegionY() + y + region.getRegionHeight()/2);
						}
					}
					alpha -= 0.2f;
				}
				
			}
		}

		Texture tex = new Texture(pix);

		Texture old = atlas.findRegion("grass").getTexture();
		for(AtlasRegion region : atlas.getRegions()){
			if(region.getTexture().equals(old))
				region.setTexture(tex);
		}
		old.dispose();
	}

	public void init(){
		player = getModule(ClientData.class).player;
		world = getModule(World.class);
		ParticleRenderer.loadParticles(this);
	}

	@Override
	public void update(){
		updateCamera();
		batch.setProjectionMatrix(camera.combined);
		clearScreen();
		doRender();
		updateCamera();

	}

	void doRender(){
		batch.begin();
		drawMap();
		RenderableHandler.getInstance().renderAll(batch);
		batch.end();
		batch.setProjectionMatrix(matrix);
		batch.begin();
		drawGUI();
		batch.end();
		batch.setColor(Color.WHITE);
	}

	void drawMap(){
		if(Gdx.graphics.getFrameId() == 5)
			updateTiles();
		int camx = Math.round(camera.position.x / World.tilesize),
				camy = Math.round(camera.position.y / World.tilesize);

		if(lastcamx != camx || lastcamy != camy){
			updateTiles();
		}

		lastcamx = camx;
		lastcamy = camy;
	}

	public void updateTiles(){
		updateTiles(0);
	}

	/**
	 * If resetID does not equal 0, it will only updated tiles whose ID is
	 * resetID
	 */
	public void updateTiles(int resetID){

		int camx = Math.round(camera.position.x / World.tilesize),
				camy = Math.round(camera.position.y / World.tilesize);

		for(int chunkx = 0; chunkx < World.loadrange * 2; chunkx++){
			for(int chunky = 0; chunky < World.loadrange * 2; chunky++){
				Chunk chunk = world.chunks[chunkx][chunky];
				if(chunk == null)
					continue;
				for(int x = 0; x < World.chunksize; x++){
					for(int y = 0; y < World.chunksize; y++){
						int worldx = chunk.worldX() + x;
						int worldy = chunk.worldY() + y;
						int rendx = chunkx * World.chunksize + x, rendy = chunky * World.chunksize + y;

						Tile tile = chunk.tiles[x][y];

						if(resetID != 0 && (tile.blockid != resetID && tile.tileid != resetID))
							continue;

						if(renderables[rendx][rendy] != null)
							renderables[rendx][rendy].free();
						if(Math.abs(worldx - camx) > viewrangex || Math.abs(worldy - camy) > viewrangey)
							continue;

						if(Math.abs(lastcamx - camx) > viewrangex || Math.abs(lastcamy - camy) > viewrangey)
							continue;

						if(renderables[rendx][rendy] != null){
							renderables[rendx][rendy].free();
						}else{
							renderables[rendx][rendy] = new RenderableList();
						}

						if(!tile.tileEmpty() && Math
								.abs(worldx * 12 - camera.position.x + 6) < camera.viewportWidth / 2 * camera.zoom + 24
								&& Math.abs(
										worldy * 12 - camera.position.y + 6) < camera.viewportHeight / 2 * camera.zoom
												+ 24){
							tile.tile().getType().draw(renderables[rendx][rendy], tile.tile(), tile, worldx, worldy);
						}

						if(!tile.blockEmpty()
								&& Math.abs(
										worldx * 12 - camera.position.x + 6) < camera.viewportWidth / 2 * camera.zoom
												+ 12 + tile.tile().getType().size()
								&& Math.abs(
										worldy * 12 - camera.position.y + 6) < camera.viewportHeight / 2 * camera.zoom
												+ 12 + tile.tile().getType().size()){
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
		font.draw(batch, launcher, gwidth() / 2 - layout.width / 2, gheight());

		font.setColor(Color.WHITE);

		if(debug){
			GridPoint2 cursor = getModule(Input.class).cursorblock();
			float cx = Gdx.input.getX() / GUIscale,
					cy = Gdx.graphics.getHeight() / GUIscale - Gdx.input.getY() / GUIscale;
			if(!world.inBounds(cursor.x, cursor.y)){
				font.draw(batch, "Out of bounds.", cx, cy);

				return;
			}
			Tile tile = world.getTile(cursor);

			Chunk chunk = world.getRelativeChunk(cursor.x, cursor.y);
			font.draw(batch,
					cursor.x + ", " + cursor.y + " " + tile + " chunk: " + chunk.x + "," + chunk.y
							+ "\nchunk block pos: " + (cursor.x - chunk.worldX()) + ", " + (cursor.y - chunk.worldY())
							+ "\n" + "chunk pos: " + chunk.x + ", " + chunk.y,
					cx, cy);

			if(tile.blockdata instanceof InventoryTileData){
				InventoryTileData data = tile.getBlockData(InventoryTileData.class);
				font.draw(batch, data.inventory.toString(), cx, cy);
			}
		}

	}

	void drawRenderables(Array<Renderable> renderables){
		batch.end();

		Array<FrameBufferLayer> blayers = new Array<FrameBufferLayer>(FrameBufferLayer.values());

		FrameBufferLayer selected = null;

		if(gbuffer)
			buffers.begin("global");

		batch.begin();

		for(Renderable layer : renderables){

			boolean ended = false;

			if(selected != null && (!selected.layerEquals(layer))){
				endBufferLayer(selected, blayers);
				selected = null;
				ended = true;
			}

			if(selected == null){

				for(FrameBufferLayer fl : blayers){
					if(fl.layerEquals(layer)){
						if(ended)
							layer.draw(batch);
						selected = fl;
						beginBufferLayer(selected);
						break;
					}
				}
			}

			layer.draw(batch);
		}
		if(selected != null){
			endBufferLayer(selected, blayers);
			selected = null;
		}
		batch.end();

		if(gbuffer)
			buffers.end("global");
		batch.begin();

		batch.setColor(Color.WHITE);
		if(gbuffer)
			batch.draw(buffers.texture("global"), camera.position.x - camera.viewportWidth / 2 * camera.zoom,
					camera.position.y + camera.viewportHeight / 2 * camera.zoom, camera.viewportWidth * camera.zoom,
					-camera.viewportHeight * camera.zoom);

	}

	private void beginBufferLayer(FrameBufferLayer selected){
		selected.beginDraw(this, batch, camera, buffers.get(selected.name));

		batch.end();
		if(gbuffer)
			buffers.end("global");

		buffers.begin(selected.name);
		buffers.get(selected.name).getColorBufferTexture().bind(selected.bind);
		for(Texture t : atlas.getTextures())
			t.bind(0);

		if(selected.shader != null)
			batch.setShader(selected.shader);
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void endBufferLayer(FrameBufferLayer selected, Array<FrameBufferLayer> layers){
		batch.end();
		if(selected.shader != null)
			batch.setShader(null);
		buffers.end(selected.name);
		buffers.get(selected.name).getColorBufferTexture().bind(0);
		if(gbuffer)
			buffers.begin("global");
		batch.begin();
		selected.end();
		batch.setColor(Color.WHITE);
		if(layers != null)
			layers.removeValue(selected, true);
	}

	void updateCamera(){
		camera.position.set(player.getX(), (player.getY()), 0f);
		camera.update();
	}

	public void resize(int width, int height){
		matrix.setToOrtho2D(0, 0, width / GUIscale, height / GUIscale);
		camera.setToOrtho(false, width / scale, height / scale); // resize
																	// camera
	}

	public GlyphLayout getBounds(String text){
		layout.setText(font, text);
		return layout;
	}

	// returns screen width / scale
	public float uiwidth(){
		return Gdx.graphics.getWidth() / GUIscale;
	}

	// returns screen height / scale
	public float uiheight(){
		return Gdx.graphics.getHeight() / GUIscale;
	}

	public TextureAtlas atlas(){
		return atlas;
	}

	public TextureRegion getRegion(String name){
		return atlas.findRegion(name);
	}

	public OrthographicCamera camera(){
		return camera;
	}

	public SpriteBatch batch(){
		return batch;
	}

	public BitmapFont font(){
		return font;
	}
}
