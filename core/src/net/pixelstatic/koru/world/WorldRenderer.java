package net.pixelstatic.koru.world;

import net.pixelstatic.koru.modules.Renderer;
import net.pixelstatic.koru.modules.World;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer{
	public static final int viewrange = 20;
	private OrthographicCamera camera;
	private World world;
	private Renderer renderer;
	private Rectangle viewBounds;
	private Sprite sprite;

	static final int NUM_VERTICES = 20;
	private float vertices[] = new float[NUM_VERTICES];

	{
		sprite = new Sprite();
	}

	public void draw(){
		setView();
		/*
		final int layerWidth = World.worldwidth;
		final int layerHeight = World.worldheight;

		final float layerTileWidth = layerWidth * World.tilesize;
		final float layerTileHeight = layerHeight * World.tilesize;

		final int col1 = Math.max(0, (int)(viewBounds.x / layerTileWidth));
		final int col2 = Math.min(layerWidth, (int)((viewBounds.x + viewBounds.width + layerTileWidth) / layerTileWidth));

		final int row1 = Math.max(0, (int)(viewBounds.y / layerTileHeight));
		final int row2 = Math.min(layerHeight, (int)((viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight));

		float y = row2 * layerTileHeight;
		float xStart = col1 * layerTileWidth;
		final float[] vertices = this.vertices;

		float color = Color.rgba8888(Color.WHITE);

		for(int row = row2;row >= row1;row --){
			float x = xStart;
			for(int col = col1;col < col2;col ++){
				final Tile tile = world.tiles[row][col];

				sprite.setPosition(x, y);
				
				

				float x1 = x;
				float y1 = y;
				float x2 = x1 + region.getRegionWidth() * World.tilesize;
				float y2 = y1 + region.getRegionHeight() * World.tilesize;

				float u1 = region.getU();
				float v1 = region.getV2();
				float u2 = region.getU2();
				float v2 = region.getV();

				vertices[X1] = x1;
				vertices[Y1] = y1;
				vertices[C1] = color;
				vertices[U1] = u1;
				vertices[V1] = v1;

				vertices[X2] = x1;
				vertices[Y2] = y2;
				vertices[C2] = color;
				vertices[U2] = u1;
				vertices[V2] = v2;

				vertices[X3] = x2;
				vertices[Y3] = y2;
				vertices[C3] = color;
				vertices[U3] = u2;
				vertices[V3] = v2;

				vertices[X4] = x2;
				vertices[Y4] = y1;
				vertices[C4] = color;
				vertices[U4] = u2;
				vertices[V4] = v1;
				batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);

				x += layerTileWidth;
			}
			y -= layerTileHeight;
		}
		*/
	}

	private void setView(){
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);

	}

	/*
	SpriteLayer sprite = new SpriteLayer("");
	
	int camx = (int)(camera.position.x/World.tilesize), camy = (int)(camera.position.y/World.tilesize);
	for(int x = camx - viewrange; x < World.worldwidth && x < camx + viewrange; x ++){
		for(int y =camy - viewrange; y < World.worldheight && y < camy + viewrange; y ++){
			if(x < 0 || y < 0) continue;
		//	Tile tile = world.tiles[x][y];
			//if(tile.tile != Material.air)tile.tile.getType().draw(tile.tile, tile, x, y, this);
			//if(tile.block != Material.air)tile.block.getType().draw(tile.block, tile, x, y, this);
		}
	}
	*/

	public WorldRenderer(World world, Renderer renderer, OrthographicCamera camera){
		this.world = world;
		this.camera = camera;
		this.renderer = renderer;
	}
}
