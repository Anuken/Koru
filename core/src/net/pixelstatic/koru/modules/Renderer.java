package net.pixelstatic.koru.modules;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.sprites.*;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;

public class Renderer extends Module{
	
	public static final int viewrange = 21;
	final float GUIscale = 5f;
	final int scale = 4;
	World world;
	SpriteBatch batch;
	OrthographicCamera camera;
	PooledLayerList layers;
	KoruAtlas atlas;
	Matrix4 matrix;
	GlyphLayout layout;
	BitmapFont font;
	
	public KoruEntity player;
	
	
	public Renderer(Koru k){
		super(k);
		SpriteLayer.renderer = this;
		batch = new SpriteBatch();
		matrix = new Matrix4();
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/ scale, Gdx.graphics.getHeight() / scale);
		atlas = new KoruAtlas(Gdx.files.internal("sprites/koru.pack"));
		layers = new PooledLayerList();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		layout = new GlyphLayout();
		Layer.atlas = this.atlas;
		//atlas.addRegion(name, textureRegion)
	}
	
	public void init(){
		player = getModule(ClientData.class).player;
		world = getModule(World.class);
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
		drawLayers();
		batch.end();
		batch.setProjectionMatrix(matrix);
		batch.begin();
		drawGUI();
		batch.end();
		batch.setColor(Color.WHITE);
	}
	
	void drawMap(){
		int camx = (int)(camera.position.x/World.tilesize), camy = (int)(camera.position.y/World.tilesize);
		for(int x = camx - viewrange; x < World.worldwidth && x < camx + viewrange; x ++){
			for(int y =camy - viewrange; y < World.worldheight && y < camy + viewrange; y ++){
				if(x < 0 || y < 0) continue;
				Tile tile = world.tiles[x][y];
				if(tile.tile != Material.air)tile.tile.getType().drawInternal(tile.tile, tile, x, y, this, world);
				if(tile.block != Material.air)tile.block.getType().drawInternal(tile.block, tile, x, y, this, world);
			}
		}
	}
	
	public void drawGUI(){
		
	}
	
	void drawLayers(){
		layers.sort();
		for(int i = 0;i < layers.count;i ++){
			Layer layer = layers.layers[i];
			layer.Draw(this);
		}
		layers.clear();
	}

	void updateCamera(){
		camera.position.set(player.getX(), player.getY(), 0f);
		limitCamera();
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
	
	void limitCamera(){
		if(camera.position.x - camera.viewportWidth / 2 * camera.zoom < 0) camera.position.x = camera.viewportWidth / 2 * camera.zoom;
		if(camera.position.y - camera.viewportHeight / 2 * camera.zoom < 0) camera.position.y = camera.viewportHeight / 2 * camera.zoom;
		if(camera.position.x + camera.viewportWidth / 2 * camera.zoom > World.worldWidthPixels()) camera.position.x = World.worldWidthPixels() - camera.viewportWidth / 2 * camera.zoom;
		if(camera.position.y + camera.viewportHeight / 2 * camera.zoom > World.worldHeightPixels()) camera.position.y = World.worldHeightPixels() - camera.viewportHeight / 2 * camera.zoom;
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

	public Layer layer(String region, float x, float y){
		//Koru.log(region);
		return layers.getLayer().add().setTemp().set(region, x, y);
	}

	public void draw(String region, float x, float y){
		batch.draw(atlas.findRegion(region), x - atlas.RegionWidth(region) / 2, y - atlas.RegionHeight(region) / 2);
	}

	public void drawc(String region, float x, float y){
		batch.draw(atlas.findRegion(region), x, y);
	}
	
	public void drawscl(String region, float x, float y, float sclx, float scly){
		
		batch.draw(atlas.findRegion(region), x - atlas.RegionWidth(region) / 2, y - atlas.RegionHeight(region) / 2, atlas.RegionWidth(region) / 2, atlas.RegionHeight(region) / 2, atlas.RegionWidth(region), atlas.RegionHeight(region), sclx, scly, 0f);
	}

	public void drawscl(String region, float x, float y, float scl){
		batch.draw(atlas.findRegion(region), x - atlas.RegionWidth(region) / 2 * scl, y - atlas.RegionHeight(region) / 2 * scl, atlas.RegionWidth(region) * scl, atlas.RegionHeight(region) * scl);
	}

	public void draw(String region, float x, float y, float rotation){
		batch.draw(atlas.findRegion(region), x - atlas.RegionWidth(region) / 2, y - atlas.RegionHeight(region) / 2, atlas.RegionWidth(region) / 2, atlas.RegionHeight(region) / 2, atlas.RegionWidth(region), atlas.RegionHeight(region), 1f, 1f, rotation);
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
}
