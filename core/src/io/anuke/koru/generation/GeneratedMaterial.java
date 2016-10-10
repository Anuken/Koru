package io.anuke.koru.generation;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;

public class GeneratedMaterial extends GeneratedObject implements Material{
	private final MaterialType type;
	private final int id;
	
	protected GeneratedMaterial(MaterialType type, int id){
		super("gen-material-"+id);
		this.id = id;
		this.type = type;
	}
	
	String getObjectType(){
		return "material";
	}
	
	protected GeneratedMaterial(){super(null); type = null; id=0;}

	@Override
	public Color foilageColor(){
		return Color.WHITE;
	}

	@Override
	public int id(){
		return id;
	}

	@Override
	public String name(){
		return name;
	}

	@Override
	public MaterialType getType(){
		return type;
	}
	
	public String toString(){
		return name();
	}

}
