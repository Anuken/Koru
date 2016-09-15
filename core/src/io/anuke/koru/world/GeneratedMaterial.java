package io.anuke.koru.world;

import com.badlogic.gdx.graphics.Color;

public class GeneratedMaterial implements Material{
	private final MaterialType type;
	private final int id;
	
	public GeneratedMaterial(MaterialType type, int id){
		this.id = id;
		this.type = type;
	}

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
		return "gen-object-"+id;
	}

	@Override
	public MaterialType getType(){
		return type;
	}

}
