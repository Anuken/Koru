package io.anuke.koru.world;

import static io.anuke.koru.modules.World.tilesize;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.koru.Koru;
import io.anuke.koru.graphics.Layers;
import io.anuke.koru.modules.World;
import io.anuke.koru.utils.Resources;
import io.anuke.layer3d.LayerList;
import io.anuke.layer3d.LayeredObject;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.spritesystem.*;

public enum MaterialType{
	tile{

		public void draw(LayerList group, Material material, Tile tile, int x, int y){

			if(tile.block().getType() == MaterialType.block)
				return;

			int type = 0;
			if(material.variants() > 1){
				type = rand(x, y, material.variants());
			}

			LayeredObject object = new LayeredObject(Resources.region(type == 0 ? material.name() : material.name() + type));
			object.setPosition(tile(x), tile(y));
			group.add(object);
		}
	},
	grass{
		Color grasscolor = new Color(0x62962fff);

		public void draw(LayerList group, Material material, Tile tile, int x, int y){
			if(tile.block().getType() == MaterialType.block)
				return;

			int rand = rand(x, y, 16);

			LayeredObject object = new LayeredObject(Resources.region("grass" + (rand <= 8 ? rand : "1")));
			object.setPosition(tile(x), tile(y));
			object.setColor(grasscolor.r * material.foilageColor().r, grasscolor.g * material.foilageColor().g, grasscolor.b * material.foilageColor().b);
			group.add(object);

		}
	},
	water{
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
			int type = 0;
			if(material.variants() > 1){
				type = rand(x, y, material.variants());
			}

			RenderPool.sprite(Resources.region(type == 0 ? material.name() : material.name() + type)).setPosition(x * World.tilesize, y * World.tilesize).setLayer(-material.id() * 2).add(group);
			if(Koru.module(World.class).blends(x, y, material))
				RenderPool.sprite(Resources.region(material.name() + "edge")).setPosition(x * World.tilesize + World.tilesize / 2, y * World.tilesize + World.tilesize / 2).center().setLayer(-material.id() * 2 + 1).add(group);
		}

		public boolean solid(){
			return false;
		}
	},
	overlay{

		public boolean tile(){
			return false;
		}
	},
	block{
		public void draw(LayerList group, Material material, Tile tile, int x, int y){
			if(material.name().equals("stoneblock")){
				LayeredObject object = new LayeredObject(Layers.get(material.name()));
				object.setPosition(tile(x), tile(y));
				group.add(object);
			}
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
			return rectangle.set(x * World.tilesize + 1, y * World.tilesize + 1, World.tilesize - i * 2, World.tilesize - 5);
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
	torch(new Color(0x744a28ff)){
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

			new SpriteRenderable(Resources.region("torchflame1")){
				public void draw(Batch batch){
					sprite.setRegion(Resources.region("torchflame" + frame(x, y)));
					super.draw(batch);
				}
			}.setPosition(tile(x), tile(y)).centerX().setLight().add(group);

			new SpriteRenderable(Resources.region("light")){
				public void draw(Batch batch){
					sprite.setOriginCenter();
					sprite.setScale(1f + (float) Math.sin(Gdx.graphics.getFrameId() / 7f + rand(x, y, 100) / 30f) / 25f + (float) Math.random() / 20f);

					super.draw(batch);
				}
			}.setPosition(tile(x), tile(y) + 6).center().setLight().setColor(0.5f, 0.4f, 0.2f).add(group);

			SpriteRenderable sprite = new SpriteRenderable(Resources.region(material.name())){
				public void draw(Batch batch){
					sprite.draw(batch);
					batch.setColor(1, 1, 1, sprite.getColor().a);
					batch.draw(Resources.region("torchflame" + frame(x, y)), sprite.getX(), sprite.getY());
				}
			}.setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX().addShadow(group, Resources.atlas(), -material.offset()).setProvider(Sorter.object).sprite();

			sprite.add(group);
		}

		int frame(int x, int y){
			return (int) (1 + (rand(x, y, 50) + Gdx.graphics.getFrameId() / 4) % 4);
		}

		public boolean tile(){
			return false;
		}
	},
	tree(Hue.rgb(80, 53, 30)){

		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

			SpriteRenderable sprite = RenderPool.sprite(Resources.region(material.name())).setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX().addShadow(group, Resources.atlas(), -material.offset()).setProvider(Sorter.object).sprite();

			sprite.add(group);
		}

		@Override
		public int size(){
			return 180;
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
	foilage(Hue.rgb(69, 109, 29, 0.02f)){
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

			//	RenderPool.sprite(Resources.region(material.name())).setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX()
			//			.setColor(tile.tile().foilageColor()).addShadow(group, Resources.atlas(), - material.offset())
			//			.setProvider(SortProviders.object).add(group);
		}

		public boolean tile(){
			return false;
		}
	},
	tallgrassblock(){
		static final float add = 0.94f;

		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
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

			for(int i = 0; i < 2; i++){
				SpriteRenderable a = RenderPool.sprite(Resources.region("grassblock2" + blendn));
				a.setProvider(Sorter.object);

				float gadd = i == 1 ? 1f : add;
				a.sprite.setColor(grasscolor.r * gadd, grasscolor.g * gadd, grasscolor.b * gadd, 1f);

				a.setPosition(itile(x), itile(y) + yadd + i * 6);
				group.add(a);
			}

			if(!isGrass(x, y + 1)){
				SpriteRenderable sh = RenderPool.sprite(Resources.region("grassblock2" + blendn)).setAsShadow().setPosition(itile(x) + 1, tile(y) + 1 + yadd);
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
		static final float add = 0.96f;

		public void draw(RenderableList group, Material material, Tile tile, int x, int y){
			float xadd = 0;

			if(!isGrass(x, y - 1)){
				//	xadd = 2;
			}

			int iter = 4;

			for(int i = 0; i < iter; i++){
				if(i == 0 && !isGrass(x, y - 1))
					continue;
				float gadd = (i % 2 == 0 ? 1f : add);
				SpriteRenderable a = RenderPool.sprite(Resources.region("grassf1"));

				a.setProvider(Sorter.object);
				a.setColor(grasscolor.r * gadd, grasscolor.g * gadd, grasscolor.b * gadd);

				a.setPosition(itile(x), itile(y) + i * (tilesize / iter) + xadd);
				a.add(group);
			}

			if(!isGrass(x, y + 1)){
				SpriteRenderable sh = RenderPool.sprite(Resources.region("grassf1")).setAsShadow();
				sh.setPosition(itile(x) + 1, tile(y) + 2 + xadd);
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

			RenderPool.sprite(Resources.region(material.name())).setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX().addShadow(group, Resources.atlas(), -material.offset()).setProvider(Sorter.object).add(group);
		}

		public boolean tile(){
			return false;
		}
	},
	workbench{
		public void draw(RenderableList group, Material material, Tile tile, int x, int y){

			RenderPool.sprite(Resources.region(material.name())).setPosition(tile(x), tile(y) + material.offset()).setLayer(tile(y)).centerX().addShadow(group, Resources.atlas(), -material.offset()).setProvider(Sorter.object).add(group);
		}

		public boolean tile(){
			return false;
		}
	};
	;
	protected final Color grasscolor = new Color(0x62962fff).mul(0.97f, 0.97f, 0.97f, 1f);
	private Color color = null;
	protected World world;

	private MaterialType() {

	}

	private MaterialType(Color color) {
		this.color = color;
	}

	public void draw(LayerList group, Material material, Tile tile, int x, int y){

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
		return i * tilesize;
	}

	public int size(){
		return 40;
	}

	int rand(int x, int y, int scl){
		int i = (x + y * x);
		MathUtils.random.setSeed(i);
		return MathUtils.random(1, scl);
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
