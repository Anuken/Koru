package net.pixelstatic.koru.world;


import net.pixelstatic.koru.modules.Renderer;
import net.pixelstatic.koru.sprites.Layer.SortType;
import net.pixelstatic.utils.Noise;
import net.pixelstatic.utils.graphics.Hue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public enum MaterialType{
	tile{
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			renderer.layer(material.name(), tile(x), tile(y)).setTile(material.ordinal());
			if(this.world.blends(x, y, material)) renderer.layer(material.name() + "edge", tile(x), tile(y)).setTile(material.ordinal()).layer--;
		}
	},
	water{
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			float tscl = 8f;
			float s = 0.2f;
			float noise = (float)Noise.normalNoise((int)(x+Gdx.graphics.getFrameId()/tscl), (int)(y+Gdx.graphics.getFrameId()/tscl), 10f, s);
			renderer.layer(material.name(), tile(x), tile(y)).setTile(-1).setColor(new Color(1f-s+noise,1f-s+noise,1f-s+noise,0.87f));
			//renderer.layer("riverrock", tile(x), tile(y)).setTile(-2);
		}
		
		public boolean solid(){
			return true;
		}
	},
	overlay{
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			renderer.layer(material.name(), tile(x), tile(y)).setTile(256);
		}
		
		public boolean tile(){
			return false;
		}
	},
	block{
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			renderer.layer(material.name(), tile(x), tile(y) - World.tilesize / 2 + 0.5f).yLayer();//.addShadow("wallshadow");
			renderer.layer("walldropshadow", tile(x), y * World.tilesize + World.tilesize * 0.9f - World.tilesize / 2).setSort(SortType.FLOOR).setLayer(259).setScale(0.14f);
		}

		public boolean tile(){
			return false;
		}

		public boolean solid(){
			return true;
		}
	},
	chest{
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			renderer.layer(material.name(), tile(x), tile(y) - World.tilesize / 2 + 0.5f).yLayer();
		}
		
		public Rectangle getRect(int x, int y, Rectangle rectangle){
			int i = 1;
			return rectangle.set(x * World.tilesize+1, y * World.tilesize+1, World.tilesize-i*2, World.tilesize-5);
		}

		public boolean tile(){
			return false;
		}

		public boolean solid(){
			return true;
		}
	},
	hatcher{
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			chest.draw(material, tile, x, y, renderer);
		}
		
		public boolean solid(){
			return false;
		}
		
		public boolean tile(){
			return false;
		}
	},
	tree(Hue.rgb(80, 53, 30)){
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			renderer.layer(/*material.name()*/"pinetree2", tile(x), tile(y)).yLayer()/*.addBlobShadow(-3)*/;
			//renderer.layer(/*material.name()*/"pinetree2roots", tile(x)+1, tile(y)-3).setLayer(3f);
			
		}

		public boolean tile(){
			return false;
		}

		public boolean solid(){
			return true;
		}

		public Rectangle getRect(int x, int y, Rectangle rectangle){
			float width = 7;
			float height = 2;
			return rectangle.set(x * World.tilesize + width / 2, y * World.tilesize + 6 + height / 2, width, height);
		}
	},
	grass(Hue.rgb(69, 109, 29,0.02f)){
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			renderer.layer(material.name(), tile(x), tile(y)).setColor(tile.tile.foilageColor()).yLayer();
		}

		public boolean tile(){
			return false;
		}
	};
	private Color color = null;
	protected World world;
	
	private MaterialType(){
		
	}
	
	private MaterialType(Color color){
		this.color = color;
	}
	public final void drawInternal(Material material, Tile tile, int x, int y, Renderer renderer, World world){
		this.world = world;
		draw(material, tile, x, y, renderer);
	}

	public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
		renderer.layer(material.name(), tile(x), tile(y)).setTile(0);
	}

	public boolean solid(){
		return false;
	}

	public Rectangle getRect(int x, int y, Rectangle rectangle){
		return rectangle.set(x * World.tilesize, y * World.tilesize, World.tilesize, World.tilesize);
	}

	public boolean tile(){
		return true;
	}
	
	public Color getColor(){
		return color;
	}

	int tile(int i){
		return i * World.tilesize + World.tilesize / 2;
	}
}
