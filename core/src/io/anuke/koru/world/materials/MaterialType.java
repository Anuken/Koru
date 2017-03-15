package io.anuke.koru.world.materials;

import static io.anuke.koru.modules.World.tilesize;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.modules.World;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.spritesystem.RenderableList;
import io.anuke.ucore.spritesystem.Sorter;

public class MaterialType{
	public static final Color grasscolor = new Color(0x62962fff);
	
	public static final BaseMaterialType tile = new BaseMaterialType(){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			if(tile.block().getType() == block) return;
			
			sprite(material.name() + variantString(x, y, material))
					.set(utile(x), utile(y)).layer(-material.id() * 2).add(group);
			
			if(world().blends(x, y, material))
				sprite((material.name() + "edge"))
						.tile(x, y)
						.center().layer(-material.id() * 2 + 1).add(group);
		}
	};
	
	public static final BaseMaterialType grass = new BaseMaterialType(){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			int rand = rand(x,y,16);
			sprite("grass" + (rand <= 8 ? rand : "1"))
					.utile(x, y).layer(-material.id() * 2)
					.color(grasscolor.r * material.foilageTint().x,
							  grasscolor.g * material.foilageTint().y,
							  grasscolor.b * material.foilageTint().z).add(group);
			
			if(world().blends(x, y, material))
				sprite("grassedge")
						.tile(x, y)
						.color(grasscolor.r * material.foilageTint().x,
								grasscolor.g * material.foilageTint().y,
								grasscolor.b * material.foilageTint().z)
						.center().layer(-material.id() * 2 + 1).add(group);
		}
	};
	
	public static final BaseMaterialType water = new BaseMaterialType(){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			
			sprite((material.name()  + variantString(x, y, material)))
					.set(x * World.tilesize, y * World.tilesize).layer(-material.id() * 2).add(group);
			
			if(world().blends(x, y, material))
				sprite((material.name() + "edge"))
						.tile(x, y)
						.center().layer(-material.id() * 2 + 1).add(group);
		}
	};
	
	public static final BaseMaterialType block = new BaseMaterialType(false, true){
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			sprite((material.name()))
			.utile(x, y)
			.scale(0, 0.001f)
			.sort(Sorter.object).add(group);
			
			sprite(("walldropshadow"))
			.shadow()
			.tile(x, y)
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
			
			sprite((material.name() + variantString(x, y, material)))
					.set(tile(x), tile(y) + offset).layer(tile(y)).centerX()
					.sort(Sorter.object)
					.addShadow(group, -offset).add(group);
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
			sprite((material.name() + variantString(x, y, material)))
			.layer(tile(y))
			.set(tile(x), tile(y) + material.offset())
			.centerX().sort(Sorter.object)
			.addShadow(group, -material.offset()).add(group);	
		}
	};
	
	public static final BaseMaterialType tallgrassblock = new BaseMaterialType(false, false){
		static final float add = 0.94f;
		
		public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y){
			float yadd = 0;
			
			Vector3 tint = tile.topTile().foilageTint();
			
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
				
				float gadd = i == 1 ? 1f : add;
				
				sprite("grassblock2" + blendn)
				.sort(Sorter.object)
				.color(grasscolor.r * gadd * tint.x, grasscolor.g * gadd * tint.y, grasscolor.b * gadd * tint.z)
				.set(utile(x), utile(y) + yadd + i * 6)
				.add(group);
			}

			if(!isGrass(x, y + 1)){
				sprite(("grassblock2" +blendn)).shadow()
						.set(utile(x) + 1, tile(y) + 1 + yadd).add(group);
				
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
			
			Vector3 tint = tile.topTile().foilageTint();
			
			int iter = 4;

			for(int i = 0; i < iter; i++){
				if(i == 0 && !isGrass(x, y - 1)) continue;
				float gadd = (i %2== 0 ? 1f : add);
				
				sprite("grassf1")
				.sort(Sorter.object)
				.color(grasscolor.r * gadd*tint.x, grasscolor.g * gadd*tint.y, grasscolor.b * gadd*tint.z)
				.set(utile(x), utile(y) + i * (tilesize / iter) + xadd)
				.add(group);
			}

			if(!isGrass(x, y + 1)){
				sprite(("grassf1")).shadow()
				.set(utile(x)+1, tile(y)+2+ xadd)
				.add(group);
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
