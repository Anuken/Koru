package io.anuke.koru.world.materials;

import static io.anuke.koru.modules.World.tilesize;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.koru.modules.World;

public abstract class BaseMaterialType implements IMaterialType{
	public boolean tile, solid;
	public Color color = Color.WHITE;
	
	public BaseMaterialType(boolean tile, boolean solid){
		this.tile = tile;
		this.solid = solid;
	}
	
	public BaseMaterialType(){
		this(true, false);
	}
	
	protected static World world(){
		return World.instance();
	}
	
	/**Random numer, generated based on coordinates.*/
	protected int rand(int x, int y, int scl){
		int i = (x+y*x);
		MathUtils.random.setSeed(i);
	    return MathUtils.random(1, scl);
	}
	
	/**Centered tile posiiton.*/
	int tile(int i){
		return i * tilesize + tilesize / 2;
	}
	
	/**Uncentered tile position.*/
	int utile(int i){
		return i*tilesize;
	}

	@Override
	public boolean tile(){
		return tile;
	}

	@Override
	public Color getColor(){
		return color;
	}

	@Override
	public boolean solid(){
		return solid;
	}
}
