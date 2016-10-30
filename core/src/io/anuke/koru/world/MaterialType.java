package io.anuke.koru.world;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.ucore.g3d.ModelList;
import io.anuke.ucore.graphics.Hue;

public enum MaterialType{
	tile{
		public void draw(ModelList list, Material material, Tile tile, int x, int y){
			
		}
	},
	water{
		
		
		public boolean solid(){
			return true;
		}
	},
	overlay{

		public boolean tile(){
			return false;
		}
	},
	block{

		public boolean tile(){
			return false;
		}

		public boolean solid(){
			return true;
		}
	},
	chest{

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

		public boolean solid(){
			return false;
		}
		
		public boolean tile(){
			return false;
		}
	},
	tree(Hue.rgb(80, 53, 30)){
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

		public boolean tile(){
			return false;
		}
	},
	object{

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
	
	public void draw(ModelList group, Material material, Tile tile, int x, int y){
		
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
