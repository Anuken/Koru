package io.anuke.koru.items;

import java.util.ArrayList;

import io.anuke.koru.world.materials.Material;

public class BlockRecipe{
	private static ArrayList<BlockRecipe> recipes = new ArrayList<BlockRecipe>();
	private static int lastid;
	
	private final int id;
	
	Material result;
	ItemStack[] requirements;
	
	public static BlockRecipe getRecipe(int id){
		return recipes.get(id);
	}
	
	public static ArrayList<BlockRecipe> getAll(){
		return recipes;
	}
	
	public BlockRecipe(Material result){
		this(result, result.getDrops());
	}
	
	public BlockRecipe(Material result, ItemStack...req){
		this.result = result;
		this.requirements = req;
		id = lastid++;
		recipes.add(this);
	}
	
	public ItemStack[] requirements(){
		return requirements;
	}
	
	public Material result(){
		return result;
	}
	
	public int id(){
		return id;
	}
	
	protected static void recipe(Material result){
		new BlockRecipe(result);
	}
	
	protected static void recipe(Material result, ItemStack...req){
		new BlockRecipe(result, req);
	}
}
