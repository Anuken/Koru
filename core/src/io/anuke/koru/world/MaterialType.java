package io.anuke.koru.world;

import static io.anuke.koru.world.World.tilesize;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.koru.Koru;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.spritesystem.RenderableList;
import io.anuke.ucore.spritesystem.SortProviders;
import io.anuke.ucore.spritesystem.SpriteRenderable;

public enum MaterialType{
	tile{

		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

			new SpriteRenderable(Resources.region(material.name()))
					.setPosition(x * World.tilesize, y * World.tilesize).setLayer(-material.id() * 2).add(group);

			if(Koru.module(World.class).blends(x, y, material))
				new SpriteRenderable(Resources.region(material.name() + "edge"))
						.setPosition(x * World.tilesize + World.tilesize / 2, y * World.tilesize + World.tilesize / 2)
						.center().setLayer(-material.id() * 2 + 1).add(group);
		}
	},
	water{
		final float tscl = 10f;
		final float s = 0.2f;

		public void draw(final RenderableList group, final Material material, final Tile tile, final int x,
				final int y){

			new SpriteRenderable(Resources.region("riverrock")).setPosition(x * World.tilesize, y * World.tilesize)
					.setLayer(2).add(group);

			new SpriteRenderable(Resources.region(material.name())){
				public void draw(Batch batch){
					float noise = (float) Noise.normalNoise((int) (x + Gdx.graphics.getFrameId() / tscl),
							(int) (y + Gdx.graphics.getFrameId() / tscl), 10f, s);
					setColor(new Color(1f - s + noise, 1f - s + noise, 1f - s + noise, material == Materials.deepwater ? 0.93f : 0.85f));
					super.draw(batch);
				}
			}.setPosition(x * World.tilesize, y * World.tilesize).setLayer(1).setColor(
					new Color(1, 1, 1, 0.3f))
					.add(group);

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
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

		}

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
			return rectangle.set(x * World.tilesize + 1, y * World.tilesize + 1, World.tilesize - i * 2,
					World.tilesize - 5);
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

			SpriteRenderable sprite = (SpriteRenderable) new SpriteRenderable(Resources.region(material.name()))
					.setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX().addShadow(group, Resources.atlas(), -material.offset())
					.setProvider(SortProviders.object);

			sprite.add(group);

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
	grass(Hue.rgb(69, 109, 29, 0.02f)){
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
			
			new SpriteRenderable(Resources.region(material.name())).setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX()
					.setColor(tile.tile().foilageColor()).addShadow(group, Resources.atlas(), - material.offset())
					.setProvider(SortProviders.object).add(group);
		}

		public boolean tile(){
			return false;
		}
	},
	tallgrassblock(){
		static final float add2 = 0.01f;

		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

			String name = material.name();
			float yadd = 0;
			int blend = blendStage(x, y);
			String blendn = "";
			if(blend == 0)
				blendn = "edge";
			if(blend == 1)
				blendn = "left";
			if(blend == 2)
				blendn = "right";
			if(!isGrass(x, y - 1)){
				yadd = 2;
			}
			
			MathUtils.random.setSeed(x + y * x);
			
			int rand = MathUtils.random.nextInt(3) + 1;

			for(int i = 0; i < 2; i++){
				SpriteRenderable a = new SpriteRenderable(Resources.region(name + blendn));
				a.setProvider(SortProviders.object);
				if(i == 1)
					a.setRegion(Resources.region("grassblock" + (rand != 1 ? rand : "") + blendn));
				
				float add = i == 1 ? add2 : 0;
				a.sprite.setColor(82 / 255f + add, 127 / 255f + add, 38 / 255f + add, 1f);

				a.setPosition(itile(x), tile(y) + yadd);
				group.add(a);
			}

			if(!isGrass(x, y - 1) || (!isGrass(x+1, y - 1) || !isGrass(x-1, y - 1))){
				SpriteRenderable sh = new SpriteRenderable(Resources.region(name + blendn)).setAsShadow()
						.setPosition(itile(x), tile(y) - 19 + yadd);
				sh.sprite.setFlip(false, true);
				sh.add(group);
			}
		}
		
		public boolean tile(){
			return false;
		}
		
		public int size(){
			return 16;
		}
	},
	shortgrassblock{
		static final float sgadd = 0.55f;
		static final float add = 0.02f;

		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
			float xadd = 0;

			if(!isGrass(x, y - 1)){
				xadd = 2;
			}
			int rand = (Math.abs((int) (x % 10 + y % 20 + MathUtils.sin(x) * 3 + MathUtils.sin(y) + (MathUtils.sin(x)/ MathUtils.cos(x)) * 4)));
			int iter = 6;

			for(int i = 0; i < iter; i++){
				SpriteRenderable a = new SpriteRenderable(
						Resources.region("grassf" + (new Random(i + rand).nextInt(40) % 4 + 1)));
				a.setProvider(SortProviders.object);
				a.setColor(new Color(82 / 255f, 127 / 255f, 38 / 255f, 1f).mul(sgadd)
						.add((new Color(66 / 255f, 105 / 255f, 27 / 255f, 1f)).mul(1f - sgadd)));
				if(i % 2 == 0)
					a.setColor(a.sprite.getColor().add(add, add, add, 0f));
				
				a.setPosition(itile(x), itile(y) + i * (tilesize / iter) + xadd);
				a.add(group);
			}

			if(!isGrass(x, y - 1)){
				SpriteRenderable sh = new SpriteRenderable(Resources.region("grassf1")).setAsShadow();
				sh.setPosition(itile(x), itile(y) - 19 + xadd);
				sh.sprite.setFlip(false, true);
				sh.add(group);
			}
		}
		
		public int size(){
			return 16;
		}
		
		public boolean tile(){
			return false;
		}
	},
	object{
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

			new SpriteRenderable(Resources.region(material.name())).setPosition(tile(x), tile(y)).centerX()
					.addShadow(group, Resources.atlas()).setProvider(SortProviders.object).add(group);
		}

		public boolean tile(){
			return false;
		}
	};
	private Color color = null;
	protected World world;

	private MaterialType() {

	}

	private MaterialType(Color color) {
		this.color = color;
	}

	public void draw(RenderableList group, Material material, Tile tile, int x, int y){

	}

	public boolean solid(){
		return false;
	}

	public Rectangle getRect(int x, int y, Rectangle rectangle){
		return rectangle.set(x * tilesize, y * tilesize, tilesize, tilesize);
	}

	public boolean tile(){
		return true;
	}

	public Color getColor(){
		return color;
	}

	int tile(int i){
		return i * tilesize + tilesize / 2;
	}
	
	int itile(int i){
		return i*tilesize;
	}
	
	public int size(){
		return 80;
	}

	int blendStage(int x, int y){
		if(!isGrass(x + 1, y) && !isGrass(x - 1, y))
			return 0;
		if(!isGrass(x + 1, y) && isGrass(x - 1, y))
			return 1;
		if(isGrass(x + 1, y) && !isGrass(x - 1, y))
			return 2;
		return 3;
	}

	boolean isGrass(int x, int y){
		return Koru.module(World.class).isType(x, y, Materials.grassblock) || Koru.module(World.class).isType(x, y, Materials.shortgrassblock);
	}
}
