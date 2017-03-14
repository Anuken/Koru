package io.anuke.koru.world.materials;

import static io.anuke.koru.modules.World.tilesize;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.Koru;
import io.anuke.koru.graphics.RenderPool;
import io.anuke.koru.modules.World;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.spritesystem.RenderableList;
import io.anuke.ucore.spritesystem.Sorter;
import io.anuke.ucore.spritesystem.SpriteRenderable;

public class MaterialType{
	public static final Color grasscolor = new Color(0x62962fff);
	
	public static final BaseMaterialType tile = new BaseMaterialType(){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			if(tile.block().getType() == block) return;
			
			RenderPool.sprite((material.name() + variantString(x, y, material)))
					.set(utile(x), utile(y)).layer(-material.id() * 2).add(group);
			if(Koru.module(World.class).blends(x, y, material))
				RenderPool.sprite((material.name() + "edge"))
						.set(tile(x), tile(y))
						.center().layer(-material.id() * 2 + 1).add(group);
		}
	};
	
	public static final BaseMaterialType grass = new BaseMaterialType(){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			int rand = rand(x,y,16);
			RenderPool.sprite(("grass" + (rand <= 8 ? rand : "1")))
					.set(x * World.tilesize, y * World.tilesize).layer(-material.id() * 2)
					.color(grasscolor.r * material.foilageTint().x,
							  grasscolor.g * material.foilageTint().y,
							  grasscolor.b * material.foilageTint().z).add(group);
			
			if(Koru.module(World.class).blends(x, y, material))
				RenderPool.sprite(("grassedge"))
						.set(x * World.tilesize + World.tilesize / 2, y * World.tilesize + World.tilesize / 2)
						.color(grasscolor.r * material.foilageTint().x,
								grasscolor.g * material.foilageTint().y,
								grasscolor.b * material.foilageTint().z)
						.center().layer(-material.id() * 2 + 1).add(group);
		}
	};
	
	public static final BaseMaterialType water = new BaseMaterialType(){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			RenderPool.sprite((material.name()  + variantString(x, y, material)))
					.set(x * World.tilesize, y * World.tilesize).layer(-material.id() * 2).add(group);
			if(Koru.module(World.class).blends(x, y, material))
				RenderPool.sprite((material.name() + "edge"))
						.set(x * World.tilesize + World.tilesize / 2, y * World.tilesize + World.tilesize / 2)
						.center().layer(-material.id() * 2 + 1).add(group);
		}
	};
	
	public static final BaseMaterialType block = new BaseMaterialType(false, true){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			RenderPool.sprite((material.name()))
			.scale(0, 0.001f)
			.set(utile(x), utile(y))
			.sort(Sorter.object).add(group);
			
			RenderPool.sprite(("walldropshadow"))
			.shadow()
			.set(tile(x), tile(y))
			.center()
			.sort(Sorter.tile).add(group);
		}
	};
	
	public static final BaseMaterialType tree = new BaseMaterialType(false, true){
		{
			color = Hue.rgb(80, 53, 30);
		}
		
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			float offset = variantOffset(x, y, material);
			
			SpriteRenderable sprite = RenderPool.sprite((material.name() + variantString(x, y, material)))
					.addShadow(group, -offset)
					.set(tile(x), tile(y) + offset).layer(tile(y)).centerX()
					.sort(Sorter.object).sprite();

			sprite.add(group);
		}
		
		public int size(){
			return 180;
		}
		
		public Rectangle getHitbox(int x, int y, Rectangle rectangle){
			float width = 7;
			float height = 3;
			return rectangle.set(x * World.tilesize + width / 2f, y * World.tilesize + 6 + height / 2f, width, height);
		}
	};
	
	public static final BaseMaterialType object = new BaseMaterialType(false, false){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			RenderPool.sprite((material.name() + variantString(x, y, material)))
			.addShadow(group, -material.offset())
			.layer(tile(y))
			.set(tile(x), tile(y) + material.offset())
			.centerX().sort(Sorter.object).add(group);	
		}
	};
	
	public static final BaseMaterialType tallgrassblock = new BaseMaterialType(false, false){
		static final float add = 0.94f;
		
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			float yadd = 0;
			
			Vector3 tint = tile.tile().foilageTint();
			
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
				SpriteRenderable a = RenderPool.sprite(("grassblock2" + blendn));
				a.sort(Sorter.object);
				
				float gadd = i == 1 ? 1f : add;
				a.sprite.setColor(grasscolor.r * gadd * tint.x, grasscolor.g * gadd * tint.y, grasscolor.b * gadd * tint.z, 1f);

				a.set(utile(x), utile(y) + yadd + i * 6);
				group.add(a);
			}

			if(!isGrass(x, y + 1)){
				SpriteRenderable sh = RenderPool.sprite(("grassblock2" +blendn)).shadow()
						.set(utile(x) + 1, tile(y) + 1 + yadd);
				sh.add(group);
			}
		}
		
		public int size(){
			return 16;
		}
	};
	
	public static final BaseMaterialType shortgrassblock = new BaseMaterialType(false, false){
		static final float add = 0.96f;
		
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			float xadd = 0;
			
			Vector3 tint = tile.tile().foilageTint();
			
			int iter = 4;

			for(int i = 0; i < iter; i++){
				if(i == 0 && !isGrass(x, y - 1)) continue;
				float gadd = (i %2== 0 ? 1f : add);
				SpriteRenderable a = RenderPool.sprite(("grassf1"));
				
				a.sort(Sorter.object);
				a.color(grasscolor.r * gadd*tint.x, grasscolor.g * gadd*tint.y, grasscolor.b * gadd*tint.z);
				
				a.set(utile(x), utile(y) + i * (tilesize / iter) + xadd);
				a.add(group);
			}

			if(!isGrass(x, y + 1)){
				SpriteRenderable sh = RenderPool.sprite(("grassf1")).shadow();
				sh.set(utile(x)+1, tile(y)+2+ xadd);
				sh.add(group);
			}
		}
		
		public int size(){
			return 16;
		}
	};
	
	static int blendStage(int x, int y){
		if(!isGrass(x + 1, y) && !isGrass(x - 1, y))
			return 0;
		if(!isGrass(x + 1, y) && isGrass(x - 1, y))
			return 1;
		if(isGrass(x + 1, y) && !isGrass(x - 1, y))
			return 2;
		return 3;
	}

	static boolean isGrass(int x, int y){
		return World.instance().isType(x, y, Material.grassblock) || World.instance().isType(x, y, Material.shortgrassblock);
	}
}
