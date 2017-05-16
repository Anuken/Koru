package io.anuke.koru.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.bitfire.utils.ShaderLoader;

import io.anuke.gif.GifRecorder;
import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.graphics.KoruCursors;
import io.anuke.koru.graphics.KoruRenderable;
import io.anuke.koru.graphics.ProcessorSurface;
import io.anuke.koru.input.InputHandler;
import io.anuke.koru.items.BlockRecipe;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.systems.CollisionDebugSystem;
import io.anuke.koru.utils.Profiler;
import io.anuke.koru.utils.RepackableAtlas;
import io.anuke.koru.utils.Resources;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialTypes;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.core.*;
import io.anuke.ucore.lights.Light;
import io.anuke.ucore.lights.PointLight;
import io.anuke.ucore.lights.RayHandler;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.renderables.*;

public class Renderer extends RendererModule<Koru>{
	//TODO why are these final?
	public static final int viewrangex = 28;
	public static final int viewrangey = 26;
	
	public static final int scale = 4;
	public static final Color outlineColor = new Color(0.5f, 0.7f, 1f, 1f);
	
	private World world;
	private KoruEntity player;
	private ProcessorSurface surface;
	private RenderableList[][] renderables = new RenderableList[World.chunksize * World.loadrange * 2][World.chunksize * World.loadrange * 2];
	private Sprite shadowSprite;
	private int lastcamx, lastcamy;
	private GifRecorder recorder;
	
	private RayHandler rays;
	private PointLight light;
	private float darkness = 1f;
	
	public Renderer() {
		cameraScale = scale;
		
		atlas = new RepackableAtlas(Gdx.files.internal("sprites/koru.atlas"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		font.getData().markupEnabled = true;
		recorder = new GifRecorder(batch);
		
		ShaderLoader.BasePath = "default-shaders/";
		ShaderLoader.Pedantic = false;
		loadShaders();
		
		RayHandler.isDiffuse = true;
		rays = new RayHandler();
		light = new PointLight(rays, 200, Color.WHITE, 1135, 0, 0);
		light.setNoise(1, 0, 0);

		RenderableHandler.instance().setLayerManager(new DrawLayerManager());
		KoruCursors.setCursor("cursor");
		KoruCursors.updateCursor();

		loadMaterialColors();
		
		Draw.addSurface(surface = new ProcessorSurface());

		Koru.log("Loaded resources.");
	}
	
	void loadShaders(){
		Shaders.load("outline", "outline", "outline", (shader, params)->{
			shader.setUniformf("u_color", new Color((float)params[0], (float)params[1], (float)params[2], (float)params[3]));
			
		});
		
		Shaders.load("inline", "inline", "outline", (shader, params)->{
			TextureRegion region = (TextureRegion)params[4];
			
			shader.setUniformf("u_size", new Vector2(region.getRegionWidth(), region.getRegionHeight()));
			shader.setUniformf("u_pos", new Vector2(region.getRegionX(), region.getRegionY()));
			shader.setUniformf("u_color", new Color((float)params[0], (float)params[1], (float)params[2], (float)params[3]));
		});
	}

	void loadMaterialColors(){

		for(Material material : Material.getAll()){
			if(material.getType().tile())
				continue;

			TextureRegion region = atlas.findRegion(material.name());
			if(region == null)
				continue;

			Pixmap pixmap = atlas.getPixmapOf(region);

			material.color = new Color(pixmap.getPixel(region.getRegionX(), region.getRegionY()));
		}
	}
	
	@Override
	public void init(){
		player = Koru.control.player;
		world = getModule(World.class);

		Resources.loadParticle("spark");
		Resources.loadParticle("break");

		new FuncRenderable(this::drawBlockOverlay).add();
		new FuncRenderable(-Sorter.light-1, Sorter.object, this::drawTileOverlay).add();
		new FuncRenderable(-Sorter.light-1, Sorter.object, this::drawSelectOverlay).add();

		shadowSprite = new Sprite(Draw.region("lightshadow"));
		shadowSprite.setSize(52, 52);
	}

	@Override
	public void update(){
		long start = TimeUtils.nanoTime();
		updateLight();
		if(Koru.control.canMove())
			KoruCursors.setCursor("cursor");

		surface.setLightColor(world.getAmbientColor());
		
		updateCamera();
		batch.setProjectionMatrix(camera.combined);

		doRender();
		updateCamera();
		
		if(Koru.control.canMove())
			KoruCursors.updateCursor();

		if(Profiler.update())
			Profiler.renderTime = TimeUtils.timeSinceNanos(start);
	}
	
	void updateLight(){
		//TODO more performant lights?
		float w = screen.x*2;
		float h = screen.y*2;
		rays.setBounds(player.getX()-w/2, player.getY()-h/2, w, h);
		
		//debug controls?
		if(Inputs.buttonUp(Buttons.LEFT) && Inputs.keyDown(Keys.CONTROL_LEFT) && Koru.control.debug){
			Light add = new PointLight(rays, 50, Color.ORANGE, 100, Graphics.mouseWorld().x, Graphics.mouseWorld().y);
			add.setNoise(3, 2, 1.5f);
		}
		
		light.setPosition(player.getX(), player.getY()+7);
		Tile tile = world.getWorldTile(player.getX(), player.getY());
		
		if(tile == null) return;
		
		float l = tile.light();
		darkness = MathUtils.lerp(darkness, l, 0.005f);
		
		light.setColor(darkness, darkness, darkness, 1f);
	}

	void doRender(){

		Draw.surface("processor");
		clearScreen();
		drawMap();
		RenderableHandler.instance().renderAll(batch);

		if(Koru.control.debug)
			Koru.engine.getSystem(CollisionDebugSystem.class).update(0);
		
		Draw.surface(true);
		
		rays.setCombinedMatrix(camera);
		rays.updateAndRender();
		
		if(Koru.control.canMove())
			recorder.update();
	}
	
	void drawSelectOverlay(FuncRenderable r){
		if(getModule(UI.class).menuOpen()) return;

		Tile tile = world.getTile(cursorX(), cursorY());
		ItemStack stack = player.inventory().hotbarStack();
		
		if(stack != null && tile != null && playerReachesBlock()){
			Material select = null;
			
			if(stack.breaks(tile.block().breakType())){
				select = tile.block();
			}else if(stack.breaks(tile.topTile().breakType()) && tile.canRemoveTile()){
				select = tile.topTile();
			}
			
			if(select == null) return;
			
			if(stack.item.name().contains("pickaxe")){
				KoruCursors.setCursor("pickaxe");
			}else if(stack.item.name().contains("axe")){
				KoruCursors.setCursor("axe");
			}
			
			String name = select.name() + 
					select.getType().drawString(cursorX(), cursorY(), select);
			
			TextureRegion region = Draw.region(name);
			
			RenderableList list = getRenderable(cursorX(), cursorY());
			
			if(list.renderables.size == 0) return;
			
			KoruRenderable b = (KoruRenderable)(list.renderables.peek());
			
			Draw.shader("inline", outlineColor.r, outlineColor.g, outlineColor.b, 1f, region);
			b.draw();
			Draw.shader();
		}
		
	}

	void drawTileOverlay(FuncRenderable r){
		if(getModule(UI.class).menuOpen()) return;

		int x = cursorX();
		int y = cursorY();

		Tile tile = world.getTile(x, y);

		if(tile != null && tile.block().interactable() && playerReachesBlock()){
			boolean overlay = tile.block().getType() == MaterialTypes.overlay;
			KoruCursors.setCursor("select");
			
			Draw.shader("outline", outlineColor.r, outlineColor.g, outlineColor.b, 1f);
			
			Draw.crect(tile.block().name() + tile.block().getType().drawString(x, y, tile.block()), 
					x * World.tilesize, y * World.tilesize + (overlay ? 0 : 6 + tile.block().getType().variantOffset(x, y, tile.block())));

			Draw.shader();
		}
	}

	void drawBlockOverlay(FuncRenderable r){

		int x = cursorX();
		int y = cursorY();

		r.layer = y * World.tilesize;

		r.sort(Sorter.object);

		InventoryComponent inv = player.inventory();

		Tile tile = world.getTile(x, y);
		
		
		if(inv.recipe != -1 && inv.hotbarStack() != null && inv.hotbarStack().item.isType(ItemType.placer) && inv.hasAll(BlockRecipe.getRecipe(inv.recipe).requirements())){

			if(Vector2.dst(World.world(x), World.world(y), player.getX(), player.getY()) < InputHandler.reach && World.isPlaceable(BlockRecipe.getRecipe(inv.recipe).result(), tile)){
				Draw.color(0.5f, 1f, 0.5f, 0.3f);
			}else{
				Draw.color(1f, 0.5f, 0.5f, 0.3f);
			}

			Material result = BlockRecipe.getRecipe(inv.recipe).result();
			
			
			if(result.getType() == MaterialTypes.tile){
				Draw.crect("blank", x*World.tilesize, y*World.tilesize, 12, 12);
			}else{
				Draw.crect("block", x*World.tilesize, y*World.tilesize, 12, 20);
			}

			Draw.color();
		}
		
	}
	
	boolean playerReachesBlock(){
		return Vector2.dst(World.world(cursorX()), World.world(cursorY()), player.getX(), player.getY()) < InputHandler.reach;
	}

	int cursorX(){
		return World.tile(Graphics.mouseWorld().x);
	}

	int cursorY(){
		return World.tile(Graphics.mouseWorld().y);
	}

	void drawMap(){
		if(Gdx.graphics.getFrameId() == 5)
			updateTiles();
		
		int camx = Math.round(camera.position.x / World.tilesize), camy = Math.round(camera.position.y / World.tilesize);

		if(lastcamx != camx || lastcamy != camy){
			updateTiles();
		}

		lastcamx = camx;
		lastcamy = camy;
	}
	
	RenderableList getRenderable(int worldx, int worldy){
		int camx = world.toChunkCoords(camera.position.x), camy =world.toChunkCoords(camera.position.y);
		return renderables[(worldx + renderables.length/2 - camx*World.chunksize)][(worldy + renderables[0].length/2 - camy*World.chunksize)];
	}

	public void updateTiles(){
		rays.clearRects();
		
		int camx = Math.round(camera.position.x / World.tilesize), camy = Math.round(camera.position.y / World.tilesize);

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

						if(tile.topTile() != Materials.air && Math.abs(worldx * 12 - camera.position.x + 6) < camera.viewportWidth / 2 * camera.zoom + 24 && Math.abs(worldy * 12 - camera.position.y + 6) < camera.viewportHeight / 2 * camera.zoom + 36){
							tile.topTile().getType().draw(renderables[rendx][rendy], tile.topTile(), tile, worldx, worldy);

							if(tile.light < 127){
							//	RenderPool.get("lightshadow").dark().set(worldx * 12 + 6, worldy * 12 + 12).size(52, 52).center().alpha(1f - tile.light()).add(renderables[rendx][rendy]);
							}
						}

						if(!tile.blockEmpty() && Math.abs(worldx * 12 - camera.position.x + 6) < camera.viewportWidth / 2 * camera.zoom + 12 + tile.block().getType().size() && Math.abs(worldy * 12 - camera.position.y + 6) < camera.viewportHeight / 2 * camera.zoom + 12 + tile.block().getType().size()){
							tile.block().getType().draw(renderables[rendx][rendy], tile.block(), tile, worldx, worldy);
						}
						
						if(!tile.blockEmpty() && tile.block().getType() == MaterialTypes.block &&
								world.isAccesible(worldx, worldy)){
							
							Rectangle rect = tile.block().getType().getHitbox(worldx, worldy, new Rectangle());
							rect.y += World.tilesize/4;
							rays.addRect(rect);
						}
					}
				}
			}
		}
		
		rays.updateRects();
	}

	void updateCamera(){
		camera.position.set(player.getX(), (player.getY()), 0f);
		camera.update();
	}
	
	@Override
	public void resize(){
		rays.resizeFBO((int)(screen.x/4), (int)(screen.y/4));
		rays.pixelate();
	}
}
