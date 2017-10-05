package io.anuke.koru.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Json;

import io.anuke.koru.items.impl.BlockRecipes;
import io.anuke.koru.items.impl.Recipes;
import io.anuke.koru.world.materials.Materials;
import io.anuke.ucore.core.Core;

public class Resources{
	private static Json json;
	
	public static void loadData(){
		Materials.load();
		BlockRecipes.load();
		Recipes.load();
	}
	
	public static BitmapFont font(){
		return Core.font;
	}
	
	public static BitmapFont font2(){
		return Core.skin.getFont("pixel-font-noborder");
	}
	
	public static BitmapFont font3(){
		return Core.skin.getFont("pixel-font");
	}
	
	public static BitmapFont font(String name){
		return Core.skin.getFont(name);
	}
	
	public static Json getJson(){
		if(json == null) json = new Json();
		return json;
	}
}
