package net.pixelstatic.koru.world;


import net.pixelstatic.gdxutils.Noise;
import net.pixelstatic.gdxutils.graphics.Hue;
import net.pixelstatic.gdxutils.spritesystem.RenderableList;
import net.pixelstatic.gdxutils.spritesystem.SortProviders;
import net.pixelstatic.gdxutils.spritesystem.SpriteRenderable;
import net.pixelstatic.koru.modules.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public enum MaterialType{
	tile{

		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
			
			new SpriteRenderable(Renderer.i.getRegion(material.name()))
			.setPosition(x*World.tilesize, y*World.tilesize)
			.setLayer(-material.ordinal()*2).add(group);
			
			if(Renderer.i.world.blends(x, y, material)) 
			new SpriteRenderable(Renderer.i.getRegion(material.name()+ "edge"))
			.setPosition(x*World.tilesize+World.tilesize/2, y*World.tilesize+World.tilesize/2)
			.center()
			.setLayer(-material.ordinal()*2+1).add(group);

		}
	},
	water{
		final float tscl = 10f;
		final float s = 0.2f;
		
		public void draw(final RenderableList group, final Material material, final Tile tile, final int x, final int y){
			
			new SpriteRenderable(Renderer.i.getRegion("riverrock"))
			.setPosition(x*World.tilesize, y*World.tilesize)
			.setLayer(2).add(group);
			
			new SpriteRenderable(Renderer.i.getRegion("water")){
				public void draw(Batch batch){
					float noise = (float)Noise.normalNoise((int)(x+Gdx.graphics.getFrameId()/tscl), (int)(y+Gdx.graphics.getFrameId()/tscl), 10f, s);
					setColor(new Color(1f-s+noise,1f-s+noise,1f-s+noise,0.8f));
					super.draw(batch);
				}
			}.setPosition(x*World.tilesize, y*World.tilesize)
			.setLayer(1)
			.setColor(new Color(1,1,1,0.3f)).add(group);
			
		}
		
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
	
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
			
			new SpriteRenderable(Renderer.i.getRegion(material.name()))
			.setPosition(tile(x), tile(y)).centerX()
			.addShadow(group, Renderer.i.atlas)
			.setProvider(SortProviders.object).add(group);

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
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
			
			new SpriteRenderable(Renderer.i.getRegion(material.name()))
			.setPosition(tile(x), tile(y)).centerX()
			.setColor(tile.tile.foilageColor())
			.addShadow(group, Renderer.i.atlas)
			.setProvider(SortProviders.object)
			.add(group);
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
	
	public void draw(RenderableList group, Material material, Tile tile, int x, int y){
		
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
