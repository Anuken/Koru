package net.pixelstatic.koru.world;

import net.pixelstatic.koru.utils.Colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public enum Material{
	air, 
	grass(Colors.colora(69, 109, 29,0.04f)), 
	stone, 
	tallgrass1(MaterialType.grass),
	tallgrass2(MaterialType.grass),
	tallgrass3(MaterialType.grass),
	fern1(MaterialType.grass),
	fern2(MaterialType.grass),
	fern3(MaterialType.grass),
	koru1(MaterialType.grass),
	koru2(MaterialType.grass),
	koru3(MaterialType.grass),
	pinetree1(MaterialType.tree, true), 
	pinetree2(MaterialType.tree, true), 
	pinetree3(MaterialType.tree, true), 
	pinetree4(MaterialType.tree, true), 
	stoneblock(MaterialType.block),
	woodblock(MaterialType.block);
	
	private MaterialType type = MaterialType.tile;
	private Color foilageColor = Colors.colora(69, 109, 29,0.04f);
	private boolean breakable;
	
	private Material(){
		
	}
	
	private Material(MaterialType type){
		this.type = type;
	}
	
	private Material(MaterialType type, boolean breakable){
		this.type = type;
		this.breakable = breakable;
	}
	
	private Material(MaterialType type, Color foilageColor){
		this.type = type;
		this.foilageColor = foilageColor;
	}
	
	private Material(Color foilageColor){
		this.foilageColor = foilageColor;
	}
	
	public Color foilageColor(){
		return foilageColor;
	}
	
	public boolean breakable(){
		return breakable;
	}
	
	public MaterialType getType(){
		return type;
	}
	
	public static Material next(Material mat, int max){
		return values()[mat.ordinal() + MathUtils.random(max-1)];
	}
}
