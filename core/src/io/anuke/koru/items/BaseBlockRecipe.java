package io.anuke.koru.items;

import java.util.ArrayList;

import io.anuke.koru.world.materials.BaseMaterial;

public class BaseBlockRecipe{
	private static ArrayList<BaseBlockRecipe> recipes = new ArrayList<BaseBlockRecipe>();
	private static int lastid;
	
	private final int id;
	
	BaseMaterial result;
	ItemStack[] requirements;
	
	public static BaseBlockRecipe getRecipe(int id){
		return recipes.get(id);
	}
	
	public static ArrayList<BaseBlockRecipe> getAll(){
		return recipes;
	}
	
	public BaseBlockRecipe(BaseMaterial result){
		this(result, result.getDrops());
	}
	
	public BaseBlockRecipe(BaseMaterial result, ItemStack...req){
		this.result = result;
		this.requirements = req;
		id = lastid++;
		recipes.add(this);
	}
	
	public ItemStack[] requirements(){
		return requirements;
	}
	
	public BaseMaterial result(){
		return result;
	}
	
	public int id(){
		return id;
	}
	
	protected static void recipe(BaseMaterial result){
		new BaseBlockRecipe(result);
	}
	
	protected static void recipe(BaseMaterial result, ItemStack...req){
		new BaseBlockRecipe(result, req);
	}
}
