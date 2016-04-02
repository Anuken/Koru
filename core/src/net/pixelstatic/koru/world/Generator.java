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
				if(Math.random() < 0.1){
					world.tiles[x][y].setBlockMaterial(Material.stoneblock);
				}
			}
		}
		Koru.log("Generated world.");
	}
}
