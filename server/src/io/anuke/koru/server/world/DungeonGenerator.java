package io.anuke.koru.server.world;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Queue;

import io.anuke.koru.modules.World;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.StructureGenerator;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.Materials;
import io.anuke.koru.world.materials.StructMaterials;
import io.anuke.ucore.util.Geometry;
import io.anuke.ucore.util.GridMap;
import io.anuke.ucore.util.Mathf;

public class DungeonGenerator extends StructureGenerator{
	int roomSpacing = 13;
	int roomSize = 9;
	int passageSize = 5;
	double roomChance = 0.4;
	double connectChance = 0.3;
	int maxRooms = 16;
	int minRooms = 4;
	Material floor = StructMaterials.stonefloor, wall = StructMaterials.stonepillar;
	
	@Override
	protected void generate(Chunk chunk){

		if(Mathf.chance(0.02)){
			GridMap<Room> map = new GridMap<>();
			map.put(0, 0, new Room(0, 0));
			
			Queue<Room> points = new Queue<>();
			points.addFirst(map.get(0, 0));
			
			int rooms = 0;
			
			while(points.size != 0 && rooms++ < maxRooms){
				Room point = points.removeFirst();
				
				int di = 0;
				
				for(GridPoint2 direction : Geometry.getD4Points()){
					Room near = map.get(point.x + direction.x, point.y + direction.y);
					if(near == null &&
							(Mathf.chance(roomChance) || (points.size == 0 && rooms < minRooms))){
						
						Room other = new Room(point.x + direction.x, point.y + direction.y);
						map.put(other.x, other.y, other);
						points.addLast(other);
						
						point.connected[di] = true;
						other.connected[(di + 2) % 4] = true;
						drawPassage(chunk, point.x, point.y, di);
					}else if(near != null && Mathf.chance(connectChance)){
						point.connected[di] = true;
						near.connected[(di + 2) % 4] = true;
					}
					
					di ++;
				}
			}
			
			for(Room point : map.values()){
				drawRoom(chunk, point.x, point.y, point.connected);
			}
		}
	}
	
	void drawPassage(Chunk chunk, int gridx, int gridy, int direction){
		int midx = chunk.worldX() + World.chunksize/2 + gridx * roomSpacing;
		int midy = chunk.worldY() + World.chunksize/2 + gridy * roomSpacing;
		int hsize = passageSize/2;
		
		GridPoint2 point = Geometry.getD4Points()[direction];
		GridPoint2 left = Geometry.getD4Points()[Mathf.mod(direction - 1, 4)];
		GridPoint2 right = Geometry.getD4Points()[Mathf.mod(direction + 1, 4)];
		
		for(int i = 0; i < roomSpacing; i ++){
			if(Math.abs(i - roomSpacing / 2) < roomSize/2){
				for(int x = -hsize; x <= hsize; x ++){
					setTile(midx + point.x * i + x * left.x, midy + point.y * i + x * left.y, floor, Materials.air);
				}
				
				setTile(midx + point.x * i + hsize * left.x, midy + point.y * i + hsize * left.y, floor, wall);
				setTile(midx + point.x * i + hsize * right.x, midy + point.y * i + hsize * right.y, floor, wall);
			}
		}
	}
	
	void drawRoom(Chunk chunk, int gridx, int gridy, boolean[] connected){
		int midx = chunk.worldX() + World.chunksize/2;
		int midy = chunk.worldY() + World.chunksize/2;
		
		int hsize = passageSize/2;
		
		for(int x = 0; x < roomSize; x++){
			for(int y = 0; y < roomSize; y++){

				Material floor = this.floor, wall = Materials.air;

				if((x == 0 || y == 0 || x == roomSize - 1 || y == roomSize - 1) && !(
						(connected[1] && y == roomSize - 1 && Math.abs(x - roomSize/2) < hsize) ||
						(connected[3] && y == 0 && Math.abs(x - roomSize/2) < hsize) ||
						(connected[2] && x == 0 && Math.abs(y - roomSize/2)  <hsize) ||
						(connected[0] && x == roomSize -1 && Math.abs(y - roomSize/2) < hsize)
						)){
					wall = this.wall;
				}
				
				setTile(x + midx - roomSize/2 + gridx * roomSpacing, 
						y + midy - roomSize/2 + gridy * roomSpacing, 
						floor, wall);
			}
		}
	}
	
	class Room extends GridPoint2{
		boolean[] connected = new boolean[4];
		
		public Room(int x, int y){
			set(x, y);
		}
	}
}
