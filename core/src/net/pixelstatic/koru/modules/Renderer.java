package net.pixelstatic.koru.modules;

import net.pixelstatic.gdxutils.graphics.Atlas;
import net.pixelstatic.gdxutils.graphics.FrameBufferMap;
import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.GroupComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.graphics.FrameBufferLayer;
import net.pixelstatic.koru.renderers.ParticleRenderer;
import net.pixelstatic.koru.utils.Point;
import net.pixelstatic.koru.world.*;
import net.pixelstatic.utils.io.GifRecorder;
import net.pixelstatic.utils.modules.Module;
import net.pixelstatic.utils.spritesystem.RenderableGroup;
import net.pixelstatic.utils.spritesystem.RenderableList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class Renderer extends Module<Koru>{
	public static final int viewrangex = 21;
	public static final int viewrangey = 15;
	public static Renderer i;
	public final float GUIscale = 5f;
	public final int scale = 4;
	public World world;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public Atlas atlas;
	public Matrix4 matrix;
	public GlyphLayout layout;
	public BitmapFont font;
	public GifRecorder recorder;
	public FrameBufferMap buffers;
	public boolean debug = false;
	public final boolean gbuffer = false;
	public KoruEntity player;
	public RenderableGroup[][] renderables = new RenderableGroup[World.chunksize*World.loadrange*2][World.chunksize*World.loadrange*2];
	public int lastcamx, lastcamy;

	public Renderer(){
		i = this;
		batch = new SpriteBatch();
		matrix = new Matrix4();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);
		atlas = new Atlas(Gdx.files.internal("sprites/koru.pack"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		layout = new GlyphLayout();
		//buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		buffers = new FrameBufferMap();
		recorder = new GifRecorder(batch, 1f / GUIscale);
		FrameBufferLayer.loadShaders();

		if(gbuffer) buffers.add("global", Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
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
		RenderableList.getInstance().renderAll(batch);
		batch.end();
		batch.setProjectionMatrix(matrix);
		batch.begin();
		drawGUI();
		batch.end();
		batch.setColor(Color.WHITE);
	}

	void drawMap(){
		
		int camx = Math.round(camera.position.x / World.tilesize), camy = Math.round(camera.position.y / World.tilesize);
		
		if(lastcamx != camx || lastcamy != camy){
			updateTiles();
		}
		
		lastcamx = camx;
		lastcamy = camy;
	}
	
	public void updateTiles(){
		int camx = Math.round(camera.position.x / World.tilesize), camy = Math.round(camera.position.y / World.tilesize);
		
		for(int chunkx = 0;chunkx < World.loadrange * 2;chunkx ++){
			for(int chunky = 0;chunky < World.loadrange * 2;chunky ++){
				Chunk chunk = world.chunks[chunkx][chunky];
				if(chunk == null) continue;
				for(int x = 0;x < World.chunksize;x ++){
					for(int y = 0;y < World.chunksize;y ++){
						int worldx = chunk.worldX() + x;
						int worldy = chunk.worldY() + y;
						int rendx = chunkx*World.chunksize+x, rendy = chunky*World.chunksize+y;
						
						if(Math.abs(worldx - camx) > viewrangex || Math.abs(worldy - camy) > viewrangey) continue;

						Tile tile = chunk.tiles[x][y];
						
						if(renderables[rendx][rendy] != null){
							renderables[rendx][rendy].free();
						}else{
							renderables[rendx][rendy] = new RenderableGroup();
						}
						
						if(tile.tile != Material.air){
							tile.tile.getType().draw(renderables[rendx][rendy], tile.tile, tile, worldx, worldy);
						}
						
						if(tile.block != Material.air){
							tile.block.getType().draw(renderables[rendx][rendy], tile.block, tile, worldx, worldy);
						}
					//	if(){
							
						//}

						//if(tile.tile != Material.air) tile.tile.getType().drawInternal(tile.tile, tile, chunk.worldX() + x, chunk.worldY() + y, this, world);
						//if(tile.block != Material.air) tile.block.getType().drawInternal(tile.block, tile, chunk.worldX() + x, chunk.worldY() + y, this, world);
					}
					
				}
			}
		}
	}

	public void drawGUI(){
		font.getData().setScale(1 / GUIscale);
		font.setColor(Color.WHITE);

		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS", 0, Gdx.graphics.getHeight() / GUIscale);

		recorder.update(atlas.findRegion("blank"), Gdx.graphics.getDeltaTime() * 60f);

		if(debug){
			Point cursor = getModule(Input.class).cursorblock();
			float cx = Gdx.input.getX() / GUIscale, cy = Gdx.graphics.getHeight() / GUIscale - Gdx.input.getY() / GUIscale;
			if( !world.inBounds(cursor.x, cursor.y)){
				font.draw(batch, "Out of bounds.", cx, cy);

				return;
			}
			Tile tile = world.getTile(cursor);

			int i = 0;

			i ++;

			Chunk chunk = world.getRelativeChunk(cursor.x, cursor.y);
			font.draw(batch, cursor.x + ", " + cursor.y + " " + tile + " chunk: " + chunk.x + "," + chunk.y + "\nchunk block pos: " + (cursor.x - chunk.worldX()) + ", " + (cursor.y - chunk.worldY()) + "\n" + "chunk pos: " + chunk.x + ", " + chunk.y, cx, cy);

			if(tile.blockdata instanceof InventoryTileData){
				InventoryTileData data = tile.getBlockData(InventoryTileData.class);
				font.draw(batch, data.inventory.toString(), cx, cy);
				i += data.inventory.inventory[0].length;
			}

			for(Entity e : t.engine.getEntitiesFor(Family.all(GroupComponent.class, PositionComponent.class).get())){
				KoruEntity entity = (KoruEntity)e;
				if(entity.position().blockX() == cursor.x && entity.position().blockY() == cursor.y){
					font.draw(batch, entity.getID() + "", cx, cy + i * 5);
					i ++;
				}

			}
		}

	}
	/*
	void drawLayers(){
		batch.end();
		layers.sort();

		Array<FrameBufferLayer> blayers = new Array<FrameBufferLayer>(FrameBufferLayer.values());

		FrameBufferLayer selected = null;
		
		//Koru.log("--start--");

		if(gbuffer) buffers.begin("global");

		batch.begin();

		for(int i = 0;i < layers.count;i ++){
			Layer layer = layers.layers[i];

			boolean ended = false;

			if(selected != null && ( !selected.layerEquals(layer) || layer.sort != SortType.FLOOR)){
				//Koru.log("ending buffer " + selected + " " + i + " invalid layer " + layer.region);
				endBufferLayer(selected, blayers);
				selected = null;
				ended = true;
			}

			if(selected == null && layer.sort == SortType.FLOOR){

				for(FrameBufferLayer fl : blayers){
					if(fl.layerEquals(layer)){
						if(ended) layer.draw(this);
						//Koru.log("begin layer " + fl);
						selected = fl;
						beginBufferLayer(selected);
						break;
					}
				}
			}
			//if(layer.sort == SortType.FLOOR && layer.layer < 2) Koru.log("layer " + layer.region);
			layer.draw(this);
		}
		if(selected != null){
			endBufferLayer(selected, blayers);
			selected = null;
		}
		batch.end();

		if(gbuffer) buffers.end("global");
		batch.begin();

		batch.setColor(Color.WHITE);
		if(gbuffer) batch.draw(buffers.texture("global"), camera.position.x - camera.viewportWidth / 2 * camera.zoom, camera.position.y + camera.viewportHeight / 2 * camera.zoom, camera.viewportWidth * camera.zoom, -camera.viewportHeight * camera.zoom);
		
	}
	*/

	private void beginBufferLayer(FrameBufferLayer selected){
		selected.beginDraw(this, batch, camera, buffers.get(selected.name));
	
		batch.end();
		if(gbuffer) buffers.end("global");

		buffers.begin(selected.name);
		buffers.get(selected.name).getColorBufferTexture().bind(selected.bind);
		for(Texture t : atlas.getTextures())
			t.bind(0);

		if(selected.shader != null) batch.setShader(selected.shader);
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void endBufferLayer(FrameBufferLayer selected, Array<FrameBufferLayer> layers){
		batch.end();
		if(selected.shader != null) batch.setShader(null);
		buffers.end(selected.name);
		buffers.get(selected.name).getColorBufferTexture().bind(0);
		if(gbuffer) buffers.begin("global");
		batch.begin();
		selected.end();
		batch.setColor(Color.WHITE);
		if(layers != null) layers.removeValue(selected, true);
	}

	void updateCamera(){
		camera.position.set(player.getX(), (player.getY()), 0f);
		camera.update();
	}

	void clearScreen(){
		Color clear = Color.SKY.cpy().sub(0.1f, 0.1f, 0.1f, 0f);
		Gdx.gl.glClearColor(clear.r, clear.g, clear.b, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public void resize(int width, int height){
		matrix.setToOrtho2D(0, 0, width / GUIscale, height / GUIscale);
		camera.setToOrtho(false, width / scale, height / scale); //resize camera
	}

	public GlyphLayout getBounds(String text){
		layout.setText(font, text);
		return layout;
	}

	public void drawFont(String text, float x, float y){
		layout.setText(font, text);
		font.draw(batch, text, x - layout.width / 2, y + layout.height / 2);
	}

	//returns screen width / scale
	public float gwidth(){
		return Gdx.graphics.getWidth() / GUIscale;
	}

	//returns screen height / scale
	public float gheight(){
		return Gdx.graphics.getHeight() / GUIscale;
	}

	public void draw(String region, float x, float y){
		batch.draw(atlas.findRegion(region), (int)(x - atlas.regionWidth(region) / 2), (int)(y - atlas.regionHeight(region) / 2));
	}

	public void drawc(String region, float x, float y){
		batch.draw(atlas.findRegion(region), (int)x, (int)y);
	}

	public void drawscl(String region, float x, float y, float sclx, float scly){

		batch.draw(atlas.findRegion(region), (int)(x - atlas.regionWidth(region) / 2), (int)(y - atlas.regionHeight(region) / 2), atlas.regionWidth(region) / 2, atlas.regionHeight(region) / 2, atlas.regionWidth(region), atlas.regionHeight(region), sclx, scly, 0f);
	}

	public void draw(String region, float x, float y, float rotation){
		batch.draw(atlas.findRegion(region), x - atlas.regionWidth(region) / 2, y - atlas.regionHeight(region) / 2, atlas.regionWidth(region) / 2, atlas.regionHeight(region) / 2, atlas.regionWidth(region), atlas.regionHeight(region), 1f, 1f, rotation);
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
