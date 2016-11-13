package io.anuke.koru.modules;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.renderers.ParticleRenderer;
import io.anuke.koru.utils.RepackableAtlas;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.InventoryTileData;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.World;
import io.anuke.ucore.UCore;
import io.anuke.ucore.g3d.ModelHandler;
import io.anuke.ucore.g3d.ModelList;
import io.anuke.ucore.g3d.ModelTransform;
import io.anuke.ucore.g3d.Models;
import io.anuke.ucore.graphics.FrameBufferMap;
import io.anuke.ucore.modules.Module;

public class Renderer extends Module<Koru>{
	public static final int viewrangex = 28;
	public static final int viewrangey = 28;
	public static Renderer i;
	public final float GUIscale = 5f;
	public final int scale = 4;
	public World world;
	public SpriteBatch batch;
	public ModelBatch mbatch;
	public Environment environment;
	public Camera camera;
	public RepackableAtlas atlas;
	public GlyphLayout layout;
	public BitmapFont font;
	public FrameBufferMap buffers;
	public boolean debug = true;
	public KoruEntity player;
	public ModelList[][] renderables = new ModelList[World.chunksize * World.loadrange * 2][World.chunksize
			* World.loadrange * 2];
	public int lastcamx = -99, lastcamy = -99;
	public ModelCache models;
	public int lastchunkx, lastchunky;
	public boolean renderingTiles;
	public Array<ModelTransform> transforms = new Array<ModelTransform>();
	public long lf = 0;

	public Renderer() {
		UCore.maximizeWindow();
		i = this;
		batch = new SpriteBatch();
		mbatch = new ModelBatch();
		camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		atlas = new RepackableAtlas(Gdx.files.internal("sprites/koru.atlas"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		layout = new GlyphLayout();
		buffers = new FrameBufferMap();
		models = new ModelCache();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		ModelHandler.instance().setEnvironment(environment);

		/*
		 * ObjLoader loader = new ObjLoader(); Model model =
		 * loader.loadModel(Gdx.files.internal("models/char.obj"));
		 * ModelInstance instance = new ModelInstance(model);
		 * instance.transform.rotate(new Vector3(1,0,0), 45);
		 */
		// ModelHandler.instance().add(instance);

		for(int x = 0; x < World.chunksize * World.loadrange * 2; x++)
			for(int y = 0; y < World.chunksize * World.loadrange * 2; y++)
				renderables[x][y] = new ModelList();
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
		Gdx.gl.glCullFace(GL20.GL_BACK);
		mbatch.begin(camera);
		// if(!(Boolean)UCore.getPrivate(models, "building"))
		// mbatch.render(models, environment);
		ModelHandler.instance().render(mbatch);
		mbatch.end();

		batch.begin();
		drawGUI();
		batch.end();
		batch.setColor(Color.WHITE);
		updateCamera();
	}

	void updateCamera(){
		camera.position.set(player.getX(), 300f, player.getY() - 70f);
		camera.lookAt(player.getX(), 0f, player.getY());
		camera.far = 1000f;
		camera.update();
	}

	void updateMap(){
		if(Gdx.graphics.getFrameId() == 5)
			updateTiles();
		int camx = Math.round(player.getX() / World.tilesize), camy = Math.round(player.getY() / World.tilesize);

		if(lastcamx != camx || lastcamy != camy){
			updateTiles();
		}

		if(renderingTiles){
			renderTiles();
		}

		lastcamx = camx;
		lastcamy = camy;
	}

	public void updateTiles(){
		// updateTiles(0);
		if(!renderingTiles)
			beginRenderTiles();
	}

	/**
	 * If resetID does not equal 0, it will only updated tiles whose ID is
	 * resetID
	 */
	public void updateTiles(int resetID){
		int camx = Math.round(player.getX() / World.tilesize), camy = Math.round(player.getY() / World.tilesize);

		for(int chunkx = 0; chunkx < World.loadrange * 2; chunkx++){
			for(int chunky = 0; chunky < World.loadrange * 2; chunky++){
				Chunk chunk = world.chunks[chunkx][chunky];
				if(chunk == null)
					continue;
				for(int x = 0; x < World.chunksize; x++){
					for(int y = 0; y < World.chunksize; y++){
						render(chunk, chunkx, chunky, x, y, camx, camy);
					}
				}
			}
		}
	}

	public void beginRenderTiles(){
		lastchunkx = 0;
		lastchunky = 0;
		renderingTiles = true;
		transforms.clear();
	}

	public void renderTiles(){
		int camx = Math.round(player.getX() / World.tilesize), camy = Math.round(player.getY() / World.tilesize);

		for(int lastchunkx = 0; lastchunkx < World.loadrange * 2; lastchunkx++){
			this.lastchunkx = lastchunkx;
			for(int lastchunky = 0; lastchunky < World.loadrange * 2; lastchunky++){

				Chunk chunk = world.chunks[lastchunkx][lastchunky];
				if(chunk == null)
					continue;
				for(int x = 0; x < World.chunksize; x++){
					for(int y = 0; y < World.chunksize; y++){
						render(chunk, lastchunkx, lastchunky, x, y, camx, camy);
					}
				}
			}
		}

		renderingTiles = false;
	}

	private void render(Chunk chunk, int chunkx, int chunky, int x, int y, int camx, int camy){
		int worldx = chunk.worldX() + x;
		int worldy = chunk.worldY() + y;
		int rendx = chunkx * World.chunksize + x, rendy = chunky * World.chunksize + y;

		Tile tile = chunk.tiles[x][y];

		// if(resetID != 0 && (tile.blockid != resetID && tile.tileid !=
		// resetID)) return;

		if(Math.abs(worldx - camx) > viewrangex || Math.abs(worldy - camy) > viewrangey){
			renderables[rendx][rendy].free();
			return;
		}

		if(Math.abs(worldx - lastcamx) < viewrangex && Math.abs(worldy - lastcamy) < viewrangey)
			return;

		renderables[rendx][rendy].free();

		if(!tile.tileEmpty()){
			tile.tile().getType().draw(renderables[rendx][rendy], tile.tile(), tile, worldx, worldy);
		}

		if(!tile.blockEmpty()){
			tile.block().getType().draw(renderables[rendx][rendy], tile.block(), tile, worldx, worldy);
		}
	}

	public void drawGUI(){
		if(Gdx.input.isKeyJustPressed(Keys.R)){
			for(int x = 0; x < World.chunksize * World.loadrange * 2; x++)
				for(int y = 0; y < World.chunksize * World.loadrange * 2; y++)
					renderables[x][y].free();
		}

		font.getData().setScale(1 / GUIscale);
		font.setColor(Color.WHITE);

		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS", 0, uiheight());
		font.draw(batch, "Objects: " + ModelHandler.instance().count(), 0, uiheight() - 5);
		font.draw(batch, "Peak: " + ModelHandler.instance().peak(), 0, uiheight() - 10);

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

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width / GUIscale, height / GUIscale);
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
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
