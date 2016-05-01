package net.pixelstatic.koru.behaviors.groups;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.behaviors.groups.Group.PointType;
import net.pixelstatic.koru.behaviors.groups.Group.TilePoint;
import net.pixelstatic.koru.behaviors.tasks.BreakBlockTask;
import net.pixelstatic.koru.behaviors.tasks.GrowPlantTask;
import net.pixelstatic.koru.behaviors.tasks.PlaceBlockTask;
import net.pixelstatic.koru.utils.Point;
import net.pixelstatic.koru.world.Material;
import net.pixelstatic.koru.world.PinetreeTileData;

import com.badlogic.gdx.utils.Array;


public class TreeFarmModule extends GroupModule{
	int trees = 0;
	
	@Override
	public void update(){
		
		if(trees < 6){
			Point water = world.search(Material.water, group.x, group.y, 50); //search for water
			if(water == null) return;
			int wx = water.x, wy = water.y;
			
			int rx = -1, ry = 0;
			Koru.log(water);
			for(int i = 1; i <= 6; i ++){
				Koru.log((wx + rx*i) + "," +  (wy + ry*i) + "(" + rx + "," + ry + ")");
				group.addPoint(PointType.treefarm, wx + rx*i, wy + ry*i);
				trees ++;
			}
		}else{
			Array<TilePoint> points = group.points(PointType.treefarm);
			for(TilePoint point : points){
				if(point.reserved()) continue;
				Material material = point.block();

				if(material == Material.pinesapling && !point.tile().getBlockData(PinetreeTileData.class).hasEnoughWater()){
					new GrowPlantTask(point.x, point.y).add(tasks);
				}else if(material.name().contains("pinetree")){
					new BreakBlockTask(material, point.x, point.y).add(tasks);
				}else if(material != Material.pinesapling){
					Koru.log("placing block at " + point.x + " " + point.y);
					new PlaceBlockTask(point.x, point.y, Material.pinesapling).add(tasks);
				}
			}
		}
	}
}
