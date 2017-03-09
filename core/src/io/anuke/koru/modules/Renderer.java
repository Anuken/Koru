package io.anuke.koru.modules;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.utils.ShaderLoader;

import io.anuke.gif.GifRecorder;
import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.graphics.FrameBufferLayer;
import io.anuke.koru.graphics.LightEffect;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.items.Recipes;
import io.anuke.koru.network.InputHandler;
import io.anuke.koru.utils.RepackableAtlas;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.*;
import io.anuke.ucore.graphics.FrameBufferMap;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.spritesystem.*;

public class Renderer extends Module<Koru>{
	public static final int viewrangex = 28;
	public static final int viewrangey = 26;
	public final float GUIscale = 5f;
	public final int scale = 4;
	public boolean debug = true, consoleOpen = false;
	public World world;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public RepackableAtlas atlas;
	public Matrix4 matrix;
	public GlyphLayout layout;
	public BitmapFont font;
	public FrameBufferMap buffers;
	public Vector3 vector = new Vector3();
	public PostProcessor processor;
	public LightEffect light;
	public KoruEntity player;
	public SpriteRenderable block;
	public Sprite shadowSprite;
	public RenderableList[][] renderables = new RenderableList[World.chunksize * World.loadrange * 2][World.chunksize
			* World.loadrange * 2];
	public int lastcamx, lastcamy;
	public GifRecorder recorder;
	
	private boolean init;

	public Renderer() {
		batch = new SpriteBatch();
		matrix = new Matrix4();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);
		atlas = new RepackableAtlas(Gdx.files.internal("sprites/koru.atlas"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		font.getData().markupEnabled = true;
		layout = new GlyphLayout();
		buffers = new FrameBufferMap();
		processor = new PostProcessor(false, true, true);
		recorder = new GifRecorder(batch);
		Resources.set(this);
		ShaderLoader.BasePath = "default-shaders/";
		ShaderLoader.Pedantic = false;

		RenderableHandler.instance().setLayerManager(this::drawRenderables);

		addEffects();
		loadMaterialColors();
		
		Koru.log("Loaded resources.");
	}
	
	void loadMaterialColors(){
		
		for(Materials material : Materials.values()){
			if(material.getType().tile()) continue;
			
			TextureRegion region = atlas.findRegion(material.name());
			if(region == null) continue;
			
			Pixmap pixmap = atlas.getPixmapOf(region);
			
			material.color = new Color(pixmap.getPixel(region.getRegionX(), region.getRegionY()));
		}
	}

	void addEffects(){
		if(light != null)
			light.dispose();
		light = new LightEffect(gwidth(), gheight());

		processor.addEffect(light);
	}

	public void init(){
		player = getModule(ClientData.class).player;
		world = getModule(World.class);

		Resources.loadParticle("spark");
		Resources.loadParticle("break");
		
		(block = new SpriteRenderable(Resources.region("block")).setProvider(Sorter.object).sprite()).add();
		shadowSprite = new Sprite(Resources.region("lightshadow"));
		shadowSprite.setSize(52, 52);
	}

	@Override
	public void update(){
		if(!init && processor.getCombinedBuffer().height < Gdx.graphics.getHeight()){resetProcessor(); init = true;}
		
		light.setColor(world.getAmbientColor());

		updateCamera();
		batch.setProjectionMatrix(camera.combined);

		doRender();
		updateCamera();
	}

	void doRender(){

		processor.capture();

		clearScreen();
		batch.begin();
		drawMap();
		RenderableHandler.instance().renderAll(batch);
		drawOverlay();
		Koru.getEngine().update(0);
		batch.end();

		processor.render();

		batch.setProjectionMatrix(matrix);
		recorder.update();
		batch.begin();
		drawGUI();
		batch.end();
		batch.setColor(Color.WHITE);
	}

	void drawOverlay(){

		unproject();

		if(vector.x < 0)
			vector.x -= 12;
		if(vector.y < 0)
			vector.y -= 12;
		InventoryComponent inv = player.getComponent(InventoryComponent.class);

		block.setPosition((int) (vector.x / 12) * 12, (int) (vector.y / 12) * 12);
		Tile tile = world.getTile(getModule(Input.class).cursorblock());

		if(inv.recipe != -1 && inv.hotbarStack() != null && inv.hotbarStack().item.type() == ItemType.hammer
				&& inv.hasAll(Recipes.values()[inv.recipe].requirements())){
			
			if(Vector2.dst(World.world((int) (vector.x / 12)), World.world((int) (vector.y / 12)), player.getX(), player.getY()) < InputHandler.reach && Material.isPlaceable(Recipes.values()[inv.recipe].result(), tile)){
				block.sprite.setColor(0.5f, 1f, 0.5f, 0.3f);
			}else{
				block.sprite.setColor(1f, 0.5f, 0.5f, 0.3f);
			}
			
			Material result = Recipes.values()[inv.recipe].result();

			if(result.getType() == MaterialType.tile){
				block.setRegion(Resources.region("blank"));
				block.sprite.setSize(12, 12);
			}else{
				block.setRegion(Resources.region("block"));
				block.sprite.setSize(12, 20);
				block.setProvider(Sorter.object);
			}
		}else{
			block.setColor(Color.CLEAR);
		}
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
												+ 36){
							tile.tile().getType().draw(renderables[rendx][rendy], tile.tile(), tile, worldx, worldy);
							
							if(tile.light < 127){
								RenderPool.sprite(Resources.region("lightshadow"))
								.setDark()
								.setPosition(worldx*12 + 6, worldy*12+12)
								.setSize(52, 52)
								.center()
								.setAlpha(1f-tile.light())
								.add(renderables[rendx][rendy]);
							}
						}

						if(!tile.blockEmpty()
								&& Math.abs(
										worldx * 12 - camera.position.x + 6) < camera.viewportWidth / 2 * camera.zoom
												+ 12 + tile.block().getType().size()
								&& Math.abs(
										worldy * 12 - camera.position.y + 6) < camera.viewportHeight / 2 * camera.zoom
												+ 12 + tile.block().getType().size()){
							tile.block().getType().draw(renderables[rendx][rendy], tile.block(), tile, worldx, worldy);
						}
					}
				}
			}
		}
	}

	public void drawGUI(){
		
		if(Gdx.input.isKeyJustPressed(Keys.GRAVE)) consoleOpen = !consoleOpen;
		if(Gdx.input.isKeyJustPressed(Keys.F3)) debug = !debug;
		
		font.getData().setScale(1 / GUIscale);
		font.setColor(Color.WHITE);
		
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + "[WHITE] FPS", 0, uiheight());
		if(debug){
			font.draw(batch, 
			"[CORAL]entities: " +t.engine.getEntities().size() +
			"\n[BLUE]sprite pool peak: " + Pools.get(SpriteRenderable.class).peak +
			"\n[YELLOW]renderables: " + RenderableHandler.instance().getSize() + 
			"\n[RED]ping: " + getModule(Network.class).client.getReturnTripTime()
			, 0, uiheight() - 5);
			
			font.draw(batch, 
					"[SKY]" +t.engine.getEntities().toString().replace(",", "\n"),
					 uiwidth(), uiheight(), 0, Align.topRight, false);
		}
		
		if(consoleOpen){
			font.getData().setScale(font.getData().scaleX*0.75f);
			font.draw(batch, "[CORAL]" + Koru.getLog(), 0, uiheight() - 35);
		}
		
		font.getData().setScale(1 / GUIscale);

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
				font.draw(batch, "[RED]Out of bounds.", cx, cy);

				return;
			}
			Tile tile = world.getTile(cursor);

			Chunk chunk = world.getRelativeChunk(cursor.x, cursor.y);
			font.draw(batch,
							"[GREEN]" +cursor.x + ", " + cursor.y + " " + tile
							+ "\n[CORAL]chunk block pos: " + (cursor.x - chunk.worldX()) + ", " + (cursor.y - chunk.worldY())
							+ "\n[YELLOW]" + "chunk pos: " + chunk.x + ", " + chunk.y
							+ "\n[ORANGE]pos: " + vector.x + ", " + vector.y
							+ "\n[GREEN]time: " + world.time,
					cx, cy);
		}

	}

	void drawRenderables(Array<Renderable> renderables, Batch batch){
		batch.end();

		Array<FrameBufferLayer> blayers = new Array<FrameBufferLayer>(FrameBufferLayer.values());

		FrameBufferLayer selected = null;

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
		batch.begin();

		batch.setColor(Color.WHITE);

	}

	private void beginBufferLayer(FrameBufferLayer selected){
		selected.beginDraw(this, batch, camera, buffers.get(selected.name));

		batch.end();

		processor.captureEnd();

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

		processor.captureNoClear();

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
		camera.setToOrtho(false, width / scale, height / scale);
		light.setSize(width, height);
	}
	
	void resetProcessor(){
		processor.dispose();
		processor = new PostProcessor(false, true, true);
		addEffects();
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

	public Vector3 unproject(){
		return camera.unproject(vector.set(Gdx.input.getX(), Gdx.input.getY(), 0));
	}
}
