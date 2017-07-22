package io.anuke.koru.world.materials;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.world.BreakType;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.ecs.Spark;
				
public abstract class Material{
	private static ArrayList<Material> materials = new ArrayList<Material>();
	private static int lastid;
	
	private final int id;
	private final String name;
	private final MaterialType type;
	
	protected Vector3 foilageTint = new Vector3(1f, 1f, 1f);
	protected int breaktime;
	protected boolean collisions = true;
	protected Array<ItemStack> drops = new Array<ItemStack>();
	protected float offset = 0;
	protected float[] offsets;
	protected int variants = 1;
	protected boolean interactable = false;
	protected BreakType breaktype = BreakType.stone;
	
	public Color color = Color.CLEAR;
	
	
	public static Material getMaterial(int id){
		return materials.get(id);
	}
	
	public static Iterable<Material> getAll(){
		return materials;
	}
	
	protected Material(String name, MaterialType type){
		id = lastid++;
		this.name = name;
		this.type = type;
		
		materials.add(this);
	}
	
	protected void offsets(float... offsets){
		this.offsets = offsets;
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
	
	public boolean interactable(){
		return interactable;
	}
	
	public int id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	public MaterialType getType(){
		return type;
	}
	
	public float offset(){
		return offset;
	}
	
	public Color getColor(){
		if(type.getColor() != null) return type.getColor();
		return color;
	}
	
	public BreakType breakType(){
		return breaktype;
	}
	
	public ItemStack[] getDrops(){
		return drops.toArray(ItemStack.class);
	}
	
	public void changeEvent(Tile tile){
		
	}
	
	public int variants(){
		return variants;
	}
	
	public void onInteract(Tile tile, int x, int y, Spark spark){}
	
	@Override
	public String toString(){
		return name() + ":" + id();
	}
}
