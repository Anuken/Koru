package net.pixelstatic.koru.behaviors.groups;

import net.pixelstatic.koru.behaviors.groups.Group.PointType;
import net.pixelstatic.koru.behaviors.groups.Group.TilePoint;
import net.pixelstatic.koru.behaviors.tasks.BreakBlockTask;
import net.pixelstatic.koru.behaviors.tasks.GrowPlantTask;
import net.pixelstatic.koru.behaviors.tasks.PlaceBlockTask;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.utils.Point;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.PinetreeTileData;

import com.badlogic.gdx.utils.Array;

public class TreeFarmModule extends GroupModule{
	int trees = 0;

	@Override
	public void update(){

		if(trees < 5){
			Point water = world.search(Material.water, group.x, group.y, 50); //search for water
			if(water == null) return;

			int size = 3;

			for(int x = -size; x <= size; x ++){
				for(int y = -size; y <= size; y ++){
					int worldx = water.x + x;
					int worldy = water.y + y;
					if( !World.inBounds(worldx, worldy)) continue;
					if( !world.blockSolid(worldx, worldy)){
						if(worldy % 2 == 0){
							group.addPoint(PointType.treefarm, worldx, worldy);
							trees ++;
						}else{
							new PlaceBlockTask(worldx, worldy, Material.woodfloor).add(tasks);
						}
					}
				}
			}
		}else if(tasks.size == 0){
			Array<TilePoint> points = group.points(PointType.treefarm);
			for(TilePoint point : points){
				//if(point.reserved()) continue;
				Material material = point.block();
				if(point.tile().tile != Material.grass){
					new PlaceBlockTask(point.x, point.y, Material.grass).add(tasks);
				}else if(material == Material.pinesapling && !point.tile().getBlockData(PinetreeTileData.class).hasEnoughWater()){
					new GrowPlantTask(point.x, point.y).add(tasks);
				}else if(material.name().contains("pinetree")){
					new BreakBlockTask(material, point.x, point.y).add(tasks);
				}else if(material != Material.pinesapling){
					new PlaceBlockTask(point.x, point.y, Material.pinesapling).add(tasks);
				}
			}
		}
	}
}
