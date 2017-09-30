package io.anuke.koru.world.materials;

import static io.anuke.koru.graphics.RenderPool.get;
import static io.anuke.koru.modules.World.tilesize;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.World;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.facet.FacetList;
import io.anuke.ucore.facet.Sorter;
import io.anuke.ucore.graphics.Hue;

public class MaterialTypes{
	public static final Color grasscolor = new Color(0x62962fff);
	
	public static class Floor extends Material{
		
		protected Floor(String name) {
			super(name, MaterialLayer.floor);
		}

		@Override
		public void draw(Tile tile, FacetList list){
			if(tile.wall() instanceof Wall){
				return;
			}
			
			if(Koru.world.blends(tile.x, tile.y, this)){
				get(name + "edge")
				.tile(tile)
				.center().layer(-id() * 2 + 1 - tile.top*3).add(list);
			}
			
			get(name + variantString(tile))
			.tile(tile).center().layer(-id() * 2 - tile.top*3).add(list);
		}
	}
	
	public static class Grass extends Material{
		
		protected Grass(String name) {
			super(name, MaterialLayer.floor);
			variants = 16;
		}

		@Override
		public void draw(Tile tile, FacetList list){
			int rand = tile.rand(variants);
			
			if(tile.wall() instanceof Wall){
				return;
			}
			
			if(Koru.world.blends(tile.x, tile.y, this)){
				get("grassedge")
				.tile(tile)
				.color(grasscolor.r * foilageTint().x,
						grasscolor.g * foilageTint().y,
						grasscolor.b * foilageTint().z)
				.center().layer(-id() * 2 + 1).add(list);
			}
			
			get("grass" + (rand <= 8 ? rand : "1"))
			.tile(tile).center().layer(-id() * 2)
			.color(grasscolor.r * foilageTint().x,
					  grasscolor.g * foilageTint().y,
					  grasscolor.b * foilageTint().z)
			.add(list);
		}
	}
	
	public static class Wall extends Material{

		protected Wall(String name) {
			super(name, MaterialLayer.wall);
			solid = true;
		}

		@Override
		public void draw(Tile tile, FacetList list){
			get("walldropshadow")
			.shadow()
			.tile(tile)
			.center()
			.sort(Sorter.tile).add(list);
			
			get(name)
			.set(tile.worldx(), tile.worldy() - World.tilesize/2)
			.centerX()
			.sort(Sorter.object).add(list);
		}
		
	}
	
	public static class Overlay extends Material{

		protected Overlay(String name) {
			super(name, MaterialLayer.wall);
		}

		@Override
		public void draw(Tile tile, FacetList list){
			get(name)
			.tile(tile).layer(-512*2).center().add(list);
			
			get(name).color(0, 0, 0, 0.1f)
			.set(tile.worldx(), tile.worldy()-1).layer(-512*2+1).center().add(list);
		}
		
	}
	
	public static class FloorItem extends Overlay{

		protected FloorItem(String name) {
			super(name);
			interactable = true;
		}
		
		@Override
		public void onInteract(Tile tile, Spark entity){
			entity.get(InventoryTrait.class).addItems(getDrops());
			entity.get(InventoryTrait.class).sendUpdate(entity);
				
			tile.setWall(Materials.air);
		}
	}
	
	public static class Tree extends Material{

		protected Tree(String name) {
			super(name, MaterialLayer.wall);
			cullSize = 180;
			color = Hue.rgb(80, 53, 30).mul(1.5f);
			hitbox.width = 7;
			hitbox.height = 3;
			solid = true;
		}

		@Override
		public void draw(Tile tile, FacetList list){
			float offset = variantOffset(tile);
			
			get(name + variantString(tile))
			.set(tile.worldx(), tile.worldy() + offset).layer(tile.worldy()).centerX()
			.sort(Sorter.object)
			.addShadow(list, -offset).add(list);
		}
		
	}
	
	public static class Prop extends Material{

		protected Prop(String name) {
			super(name, MaterialLayer.wall);
		}

		@Override
		public void draw(Tile tile, FacetList list){
			float offset = variantOffset(tile);
			
			get(name + variantString(tile))
			.layer(tile.worldy())
			.set(tile.worldx(), tile.worldy() + offset)
			.centerX().sort(Sorter.object)
			.addShadow(list, -offset).add(list);
		}
		
	}
	
	public static class TallGrassWall extends Material{
		static final float add = 0.94f;

		protected TallGrassWall(String name) {
			super(name, MaterialLayer.wall);
			cullSize = 16;
		}

		@Override
		public void draw(Tile tile, FacetList list){
			float yadd = 0;
			
			Vector3 tint = tile.topFloor().foilageTint();
			
			int blend = blendStage(tile.x, tile.y);
			
			String blendn = "";
			if(blend == 0)
				blendn = "edge";
			if(blend == 1)
				blendn = "left";
			if(blend == 2)
				blendn = "right";
			if(!isGrass(tile.x, tile.y - 1)){
				yadd = 2;
			}

			for(int i = 0; i < 2; i ++){
				float gadd = i == 1 ? 1f : add;
				
				get("grassblock2" + blendn)
				.sort(Sorter.object)
				.color(grasscolor.r * gadd * tint.x, grasscolor.g * gadd * tint.y, grasscolor.b * gadd * tint.z)
				.set(tile.x * tilesize, tile.y * tilesize + yadd + i * 6 - tilesize/2)
				.centerX()
				.add(list);
			}

			if(!isGrass(tile.x, tile.y + 1)){
				get("grassblock2" +blendn).shadow()
				.set(tile.worldx() + 1, tile.worldy() + 1 + yadd - tilesize/2)
				.centerX()
				.add(list);
			}
		}
	}
	
	public static class ShortGrassWall extends Material{
		static final float add = 0.96f;

		protected ShortGrassWall(String name) {
			super(name, MaterialLayer.wall);
			cullSize = 16;
		}

		@Override
		public void draw(Tile tile, FacetList list){
			float xadd = 0;
			
			Vector3 tint = tile.topFloor().foilageTint();
			
			int iter = 4;

			for(int i = 0; i < iter; i++){
				if(i == 0 && !isGrass(tile.x, tile.y - 1)) continue;
				float gadd = (i % 2 == 0 ? 1f : add);
				
				get("grassf1")
				.sort(Sorter.object)
				.color(grasscolor.r * gadd*tint.x, grasscolor.g * gadd*tint.y, grasscolor.b * gadd*tint.z)
				.set(tile.worldx(), tile.worldy() + i * (tilesize / iter) + xadd - tilesize/2)
				.centerX()
				.add(list);
			}

			if(!isGrass(tile.x, tile.y + 1)){
				get("grassf1").shadow()
				.set(tile.worldx() + 1, tile.worldy() + 4 + xadd - tilesize/2)
				.centerX()
				.add(list);
			}
		}
	}
	
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
		return Koru.world.isType(x, y, Materials.grassblock) || 
				Koru.world.isType(x, y, Materials.shortgrassblock);
	}
}
