package io.anuke.koru.items;

import com.badlogic.gdx.utils.Array;

public class Recipe{
	private static Array<Recipe> recipes = new Array<>();
	private static int lastid;
	
	private final int id;
	
	Item result;
	ItemStack[] requirements;
	
	public Recipe(Item result, ItemStack[] requirements){
		id = lastid++;
		recipes.add(this);
		
		this.result = result;
		this.requirements = requirements;
	}
	
	public ItemStack[] requirements(){
		return requirements;
	}
	
	public Item result(){
		return result;
	}
	
	public int id(){
		return id;
	}
	
	public static Recipe getRecipe(int id){
		return recipes.get(id);
	}
	
	public static Array<Recipe> getAll(){
		return recipes;
	}
	
	public static void addRecipe(Item result, ItemStack...req){
		new Recipe(result, req);
	}
}
