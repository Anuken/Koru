package net.pixelstatic.koru.world;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.modules.World;

public class Generator{
	World world;
	
	public Generator(World world){
		this.world = world;
	}
	
	public void generate(){
		for(int x = 0;x < World.worldwidth;x ++){
			for(int y = 0;y < World.worldwidth;y ++){
				world.tiles[x][y].setTileMaterial(Material.grass);
				if(Math.random() < 0.05)
					world.tiles[x][y].setBlockMaterial(Material.next(Material.pinetree1, 4));
				if(Math.random() < 0.1)
					world.tiles[x][y].setBlockMaterial(Material.next(Material.tallgrass1, 3));
				if(Math.random() < 0.03)
					world.tiles[x][y].setBlockMaterial(Material.next(Material.fern1, 3));
				if(Math.random() < 0.01)
					world.tiles[x][y].setBlockMaterial(Material.next(Material.koru1, 3));
				//if(Math.random() < 0.1)
				//	world.tiles[x][y].setTileMaterial(Material.stone);
				
			}
		}
		Koru.log("Generated world.");
	}
}
