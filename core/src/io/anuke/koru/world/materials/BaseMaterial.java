package io.anuke.koru.world.materials;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.world.Tile;
public abstract class BaseMaterial{
	private static ArrayList<BaseMaterial> materials = new ArrayList<BaseMaterial>();
	private static int lastid;
	
	private final int id;
	private final String name;
	private final BaseMaterialType type;
	
	protected Vector3 foilageTint = new Vector3(1f, 1f, 1f);
	protected int breaktime;
	protected boolean collisions = true;
	protected Array<ItemStack> drops = new Array<ItemStack>();
	protected float offset = 0;
	protected int variants = 1;
	
	public Color color = Color.CLEAR;
	
	public static BaseMaterial getMaterial(int id){
		return materials.get(id);
	}
	
	public static Iterable<BaseMaterial> getAll(){
		return materials;
	}
	
	protected BaseMaterial(String name, BaseMaterialType type){
		id = lastid++;
		this.name = name;
		this.type = type;
		
		materials.add(this);
	}
	
	protected void addDrops(ItemStack... stacks){
		drops.addAll(stacks);
	}
	
	protected void addDrop(Item item, int amount){
		drops.add(new ItemStack(item, amount));
	}

	public Vector3 foilageTint(){
		return foilageTint;
	}
	
	public int breaktime(){
		return breaktime;
	}
	
	public boolean isBreakable(){
		return breaktime > 0;
	}
	
	public boolean collisionsEnabled(){
		return collisions;
	}
	
	public int id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	public BaseMaterialType getType(){
		return type;
	}
	
	public float offset(){
		return offset;
	}
	
	public Color getColor(){
		if(type.getColor() != null) return type.getColor();
		return color;
	}
	
	public ItemStack[] getDrops(){
		return drops.toArray(ItemStack.class);
	}
	
	public void changeEvent(Tile tile){
		
	}
	
	public int variants(){
		return variants;
	}
	
	@Override
	public String toString(){
		return name() + ":" + id();
	}
}
