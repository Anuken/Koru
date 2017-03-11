package io.anuke.koru.world.materials;

import static io.anuke.koru.modules.World.tilesize;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.koru.world.Tile;
import io.anuke.ucore.spritesystem.RenderableList;

public interface IMaterialType{
	public void draw(RenderableList group, BaseMaterial material, Tile tile, int x, int y);
	
	public boolean tile();

	public Color getColor();
	
	public default int size(){
		return 24;
	}

	public boolean solid();

	public default Rectangle getHitbox(int x, int y, Rectangle rectangle){
		return rectangle.set(x * tilesize, y * tilesize, tilesize, tilesize);
	}
}
