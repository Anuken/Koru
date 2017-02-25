package io.anuke.koru.generation;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.items.ItemStack;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;

//TODO cleanup or remove class
public class GeneratedMaterial extends GeneratedObject implements Material{
	private final MaterialType type;
	private final int id;
	private ItemStack[] drops;
	
	protected GeneratedMaterial(MaterialType type, int id, ItemStack... drops){
		super("gen-material-"+id);
		this.id = id;
		this.type = type;
		this.drops = drops;
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
	
	public ItemStack[] getDrops(){
		return drops;
	}

}
