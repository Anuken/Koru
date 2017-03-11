package io.anuke.koru.world.materials;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;
import io.anuke.koru.world.Tile;

public abstract class BaseMaterial implements Material{
	private static ArrayList<Material> materials = new ArrayList<Material>();
	private static int lastid;
	
	private final int id;
	private final String name;
	private final MaterialType type;
	
	protected Vector3 foilageTint = new Vector3(1f, 1f, 1f);
	protected int breaktime;
	protected boolean collisions = true;
	protected Array<ItemStack> drops = new Array<ItemStack>();
	protected Color color = Color.CLEAR;
	protected float offset = 0;
	protected int variants = 1;
	
	public static Material getMaterial(int id){
		return materials.get(id);
	}
	
	protected BaseMaterial(String name, MaterialType type){
		id = lastid++;
		this.name = name;
		this.type = type;
	}
	
	protected void addDrops(ItemStack... stacks){
		drops.addAll(stacks);
	}
	
	protected void addDrop(Item item, int amount){
		drops.add(new ItemStack(item, amount));
	}

	@Override
	public Vector3 foilageTint(){
		return foilageTint;
	}
	
	@Override
	public int breaktime(){
		return breaktime;
	}
	
	@Override
	public boolean collisionsEnabled(){
		return collisions;
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
	
	@Override
	public float offset(){
		return offset;
	}
	
	@Override
	public Color getColor(){
		if(type.getColor() != null) return type.getColor();
		return color;
	}
	
	@Override
	public ItemStack[] getDrops(){
		return drops.toArray(ItemStack.class);
	}
	
	@Override
	public void changeEvent(Tile tile){
		
	}
	
	@Override
	public int variants(){
		return variants;
	}
}
