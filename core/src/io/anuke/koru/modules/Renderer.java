package io.anuke.koru.modules;

import static io.anuke.koru.Koru.*;
import static io.anuke.ucore.core.Core.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;

import io.anuke.koru.Koru;
import io.anuke.koru.graphics.*;
import io.anuke.koru.graphics.lsystems.LSystems;
import io.anuke.koru.input.InputHandler;
import io.anuke.koru.items.BlockRecipe;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.world.BreakType;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialLayer;
import io.anuke.koru.world.materials.MaterialTypes.*;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.core.*;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.facet.*;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.lights.Light;
import io.anuke.ucore.lights.PointLight;
import io.anuke.ucore.lights.RayHandler;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.util.Mathf;

public class Renderer extends RendererModule{
	//TODO why are these final?
	public static final int viewrangex = 32;
	public static final int viewrangey = 26;
	
	public static final int scale = 4;
	public static final Color outlineColor = new Color(0.5f, 0.7f, 1f, 1f);
	
	private Spark player;
	private FacetList[][] renderables = new FacetList[World.chunksize * World.loadrange * 2][World.chunksize * World.loadrange * 2];
	private Sprite shadowSprite;
	private int lastcamx, lastcamy;
	
	//TODO round alpha, but not colors
	public RayHandler rays;
	private PointLight light;
	private float darkness = 1f;
	
	public Renderer() {
		Timers.mark();
		Fx.blockbreak.getClass();
		
		Core.cameraScale = scale;
		
		atlas = new Atlas("sprites.atlas");
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setUseIntegerPositions(false);
		font.getData().markupEnabled = true;
		
		RayHandler.isDiffuse = true;
		rays = new RayHandler();
		rays.setBlurNum(3);
		light = new PointLight(rays, 300, Color.WHITE, 1135, 0, 0);
		light.setSoftnessLength(40f);

		Facets.instance().setLayerManager(new FacetLayerHandler(){{
			allDrawLayers.add(KoruFacetLayers.water);
		}});
		
		KoruCursors.setCursor("cursor");
		KoruCursors.updateCursor();

		loadMaterialColors();

		Koru.log("Loaded resources. Time taken: " + Timers.elapsed() + " ms.");
		
		pixelate();
		Graphics.addSurface("trees", 1);
		Graphics.addSurface("dark", Core.cameraScale, 5);
	}

	void loadMaterialColors(){

		for(Material material : Material.getAll()){
			if(material.isLayer(MaterialLayer.floor))
				continue;

			TextureRegion region = atlas.findRegion(material.name());
			if(region == null)
				continue;

			Pixmap pixmap = atlas.getPixmapOf(region);

			material.setColor(new Color(pixmap.getPixel(region.getRegionX(), region.getRegionY())));
		}
	}
	
	@Override
	public void init(){
		player = Koru.control.player;
		new BaseFacet(this::drawBlockOverlay).add();
		new BaseFacet(-Sorter.light-1, Sorter.object, this::drawTileOverlay).add();
		new BaseFacet(-Sorter.light-1, Sorter.object, this::drawSelectOverlay).add();

		shadowSprite = new Sprite(Draw.region("lightshadow"));
		shadowSprite.setSize(52, 52);
	}

	@Override
	public void update(){
		updateLight();
		
		if(Koru.control.canMove()){
			KoruCursors.setCursor("cursor");
		}
		
		float addx = 1f-(Graphics.size().x/Core.cameraScale) % 1f;
		float addy = 1f-(Graphics.size().y/Core.cameraScale) % 1f;
		
		if(Settings.getBool("smoothcam")){
			smoothCamera(player.pos().x + addx, player.pos().y + addy, 0.08f);
		}else{
			addx = addy = 0;
			camera.position.set(player.pos().x + addx, player.pos().y + addy, 0);
		}
		
		float lim = 7;
		
		if(Math.abs(player.pos().x - camera.position.x) > lim){
			camera.position.x = player.pos().x - Mathf.clamp(player.pos().x - camera.position.x, -lim, lim);
		}
		
		if(Math.abs(player.pos().y - camera.position.y) > lim){
			camera.position.y = player.pos().y - Mathf.clamp(player.pos().y - camera.position.y, -lim, lim);
		}
		
		float lastx = camera.position.x;
		float lasty = camera.position.y;
		
		camera.position.set((int)lastx + addx, (int)lasty + addy, 0);

		draw();
		
		camera.position.set(lastx, lasty, 0);
		
		if(Koru.control.canMove())
			KoruCursors.updateCursor();
	}
	
	@Override
	public void draw(){
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		if(pixelate) beginPixel();
		
		clearScreen();
		
		LSystems.cacheAll();
		
		drawMap();
		Facets.instance().renderAll();
		
		//drawShadows();
		
		if(pixelate) endPixel();
		
		Graphics.end();
		
		rays.setCombinedMatrix(camera);
		rays.updateAndRender();
		
		if(Koru.control.canMove()){
			record();
		}
	}
	
	void updateLight(){
		//TODO more performant lights?
		float w = screen.x*2;
		float h = screen.y*2;
		rays.setBounds(camera.position.x-w/2, camera.position.y-h/2, w, h);
		
		//debug controls?
		if(Inputs.buttonUp(Buttons.LEFT) && Inputs.keyDown(Keys.CONTROL_LEFT)){
			Light add = new PointLight(rays, 50, Color.WHITE, 100, Graphics.mouseWorld().x, Graphics.mouseWorld().y);
			add.setNoise(3, 2, 1.5f);
		}
		
		light.setPosition(camera.position.x, camera.position.y+7);
		Tile tile = world.getWorldTile(player.pos().x, player.pos().y);
		
		if(tile == null) return;
		
		float l = tile.light();
		darkness = MathUtils.lerp(darkness, l, 0.03f * Timers.delta());
		
		light.setColor(darkness, darkness, darkness, 1f);
		light.setDistance(Vector2.dst(0, 0, w/2f, h/2f));
	}
	
	void drawSelectOverlay(BaseFacet r){
		if(ui.menuOpen()) return;

		Tile tile = world.getTile(Koru.control.cursorX(), Koru.control.cursorY());
		ItemStack stack = player.get(InventoryTrait.class).hotbarStack();
		
		if(stack != null && tile != null && Koru.control.playerReachesBlock() && !tile.wall().interactable()){
			Material select = null;
			
			if(stack.breaks(tile.wall().breakType()) && tile.wall().isBreakable()){
				select = tile.wall();
			}else if(stack.breaks(tile.topFloor().breakType()) && tile.canRemoveTile() && tile.topFloor().isBreakable()){
				select = tile.topFloor();
			}
			
			if(select == null) return;
			
			if(select.breakType() == BreakType.stone){
				KoruCursors.setCursor("pickaxe");
			}else if(select.breakType() == BreakType.wood){
				KoruCursors.setCursor("axe");
			}
			
			FacetList list = getRenderable(Koru.control.cursorX(), Koru.control.cursorY());
			
			if(list.facets.size == 0) return;
			
			KoruFacet facet = null;
			
			for(int i = list.facets.size - 1; i >= 0; i --){
				if(list.facets.get(i) instanceof KoruFacet){
					facet = (KoruFacet)list.facets.get(i);
					break;
				}
			}
			
			Shaders.inline.region = facet.sprite;
			Shaders.inline.color = outlineColor;
			
			Graphics.end();
			Graphics.shader(Shaders.inline);
			Graphics.begin();
			facet.draw();
			Graphics.shader();
		}
		
	}

	void drawTileOverlay(BaseFacet r){
		if(ui.menuOpen()) return;

		int x = Koru.control.cursorX();
		int y = Koru.control.cursorY();

		Tile tile = world.getTile(x, y);
		
		//TODO
		if(tile != null && tile.wall().interactable() && Koru.control.playerReachesBlock()){
			KoruCursors.setCursor("select");
			
			FacetList list = getRenderable(Koru.control.cursorX(), Koru.control.cursorY());
			SpriteFacet facet = null;
			
			for(Facet check : list.facets){
				if(check instanceof SpriteFacet && !(check.provider == Sorter.tile && check.getLayer() > 0) &&
						!MathUtils.isEqual(check.getLayer(), Sorter.shadow) && 
						(facet == null || check.compareTo(facet) > 0)){
					facet = (SpriteFacet)check;
				}
			}
			
			if(facet == null) return;
			
			Shaders.outline.color = outlineColor;
			Shaders.outline.region = facet.sprite;
			
			Graphics.end();
			Graphics.shader(Shaders.outline);
			Graphics.begin();
			
			facet.draw();
			
			Graphics.shader();
		}
	}

	void drawBlockOverlay(BaseFacet r){

		int x = Koru.control.cursorX();
		int y = Koru.control.cursorY();

		r.layer = y * World.tilesize;

		r.sort(Sorter.object);

		InventoryTrait inv = player.get(InventoryTrait.class);

		Tile tile = world.getTile(x, y);
		
		if(inv.recipe != -1 && inv.hotbarStack() != null && inv.hotbarStack().item.isType(ItemType.placer) 
				&& inv.hasAll(BlockRecipe.getRecipe(inv.recipe).requirements())){

			if(Vector2.dst(World.world(x), World.world(y), player.pos().x, player.pos().y) < InputHandler.reach && World.isPlaceable(BlockRecipe.getRecipe(inv.recipe).result(), tile)){
				Draw.color(0.5f, 1f, 0.5f, 0.3f);
			}else{
				Draw.color(1f, 0.5f, 0.5f, 0.3f);
			}

			Material result = BlockRecipe.getRecipe(inv.recipe).result();
			
			if(result.isLayer(MaterialLayer.floor)){
				Draw.rect("blank", x*World.tilesize, y*World.tilesize, 12, 12);
			}else{
				Draw.grect("block", x*World.tilesize, y*World.tilesize- World.tilesize/2, 12, 20);
			}

			Draw.color();
		}
		
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
	
	FacetList getRenderable(int worldx, int worldy){
		int camx = world.toChunkCoords(camera.position.x), camy = world.toChunkCoords(camera.position.y);
		return renderables[(worldx + renderables.length/2 - camx*World.chunksize)][(worldy + renderables[0].length/2 - camy*World.chunksize)];
	}
	
	public void invalidateTiles(){
		lastcamx = -9999999;
		lastcamy = -9999999;
	}
	
	public void drawShadows(){
		int camx = Math.round(camera.position.x / World.tilesize), camy = Math.round(camera.position.y / World.tilesize);
		Graphics.surface("dark");
		
		for(int chunkx = 0; chunkx < World.loadrange * 2; chunkx++){
			for(int chunky = 0; chunky < World.loadrange * 2; chunky++){
				Chunk chunk = world.chunks[chunkx][chunky];
				if(chunk == null)
					continue;
				for(int x = 0; x < World.chunksize; x++){
					for(int y = 0; y < World.chunksize; y++){
						int worldx = chunk.worldX() + x;
						int worldy = chunk.worldY() + y;

						Tile tile = chunk.tiles[x][y];
						
						if(Math.abs(worldx - camx) > viewrangex || Math.abs(worldy - camy) > viewrangey)
							continue;

						if(Math.abs(lastcamx - camx) > viewrangex || Math.abs(lastcamy - camy) > viewrangey)
							continue;
						
						if(tile.light() < 0.999f){
							Draw.color(0, 0, 0, 1f-tile.light());
							Draw.rect("blur", tile.worldx(), tile.worldy(), 12, 12);
						}
					}
				}
			}
		}
		Draw.color();
		
		Graphics.end();
		Graphics.shader(Shaders.round);
		Graphics.begin();
		Graphics.flushSurface();
		Graphics.end();
		Graphics.shader();
		Graphics.begin();
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
							renderables[rendx][rendy] = new FacetList();
						}

						if(tile.topFloor() != Materials.air && Math.abs(worldx * 12 - camera.position.x + 6) < camera.viewportWidth / 2 * camera.zoom + 24 && Math.abs(worldy * 12 - camera.position.y + 6) < camera.viewportHeight / 2 * camera.zoom + 36){
							tile.topFloor().draw(tile, renderables[rendx][rendy]);
						}

						if(!tile.isWallEmpty() && Math.abs(worldx * 12 - camera.position.x + 6) < 
								camera.viewportWidth / 2 * camera.zoom + 12 + tile.wall().cullSize()
								&& Math.abs(worldy * 12 - camera.position.y + 6) < 
								camera.viewportHeight / 2 * camera.zoom + 12 + tile.wall().cullSize()){
							tile.wall().draw(tile, renderables[rendx][rendy]);
						}
						

						if(!tile.isWallEmpty() && tile.wall() instanceof Wall &&
								world.isAccesible(worldx, worldy) && tile.light < 127){
							
							Rectangle rect = tile.wall().getHitbox(worldx, worldy, new Rectangle());
							rect.y += World.tilesize/4;
							rect.y += 1f;
							rays.addRect(rect);
						}
					}
				}
			}
		}
		
		rays.updateRects();
	}
	
	@Override
	public void resize(){
		rays.resizeFBO((int)(screen.x/4), (int)(screen.y/4));
		rays.pixelate();
	}
}
