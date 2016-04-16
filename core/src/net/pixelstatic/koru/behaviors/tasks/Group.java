package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.world.Material;

import com.badlogic.gdx.utils.Array;

public class Group{
	public static Group instance;
	public Array<PlaceBlockTask> taskpool = new Array<PlaceBlockTask>();
	//@formatter:off
	int[][] blocks = {
		{1,0,0,0,0,0},
		{1,0,0,0,0,0},
		{0,0,0,0,0,0},
		{1,0,0,0,0,0},
		{1,0,0,0,0,0},
		{1,1,1,0,1,1},
	};
	//@formatter:on

	static{
		instance = new Group();
	}

	public Group(){
		int range = 3;
		for(int fx = 0;fx < range;fx ++){
			for(int fy = 0;fy < range;fy ++){
				int ex = 40 + fx*blocks.length;
				int ey = 40 + fy*blocks.length;
				for(int x = 0;x < blocks.length;x ++){
					for(int y = 0;y < blocks[x].length;y ++){
						int worldx = ex + x, worldy = ey + y;
						if(blocks[x][y] != 0){
							//	tasks.add(new MoveTowardTask(worldx*12+6, (worldy+2)*12+6));
							taskpool.add(new PlaceBlockTask(worldx, worldy, Material.woodblock));
						}
					}
				}
			}
		}
	}
}
