package net.pixelstatic.koru.world;

import net.pixelstatic.koru.modules.Renderer;
import net.pixelstatic.koru.modules.World;

import com.badlogic.gdx.math.Rectangle;

public enum MaterialType{
	tile,
	block{
		public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
			renderer.layer(material.name(), tile(x),tile(y)).alignBottom().yLayer();
			renderer.layer("walldropshadow", tile(x),y*World.tilesize+ World.tilesize*0.9f).setLayer(1f).setScale(0.14f);
		}
		
		public boolean tile(){
			return false;
		}
		
		public boolean solid(){
			return true;
		}
	},
	tree,
	grass;
	
	static final int tilelayer = -1;
	
	public void draw(Material material, Tile tile, int x, int y, Renderer renderer){
		renderer.layer(material.name(),tile(x),tile(y)).setLayer(tilelayer);
	}
	
	public boolean solid(){
		return false;
	}
	
	public Rectangle getRect(int x, int y, Rectangle rectangle){
		return rectangle.set(x*World.tilesize, y*World.tilesize+6, World.tilesize, World.tilesize);
	}
	
	public boolean tile(){
		return true;
	}
	
	int tile(int i){
		return i * World.tilesize + World.tilesize/2;
	}
}
