package io.anuke.koru.items;

import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;

public enum Items implements Item{
	stick, wood, pinecone, water, stone,
	woodaxe(true, 1f){
		public boolean breaks(Material mat){
			return mat.getType() == MaterialType.tree || mat.name().contains("wood");
		}
	};
	float power;
	boolean tool;
	
	private Items(){}
	
	private Items(boolean tool, float power){
		this.tool = tool;
		this.power = power;
	}
	
	private Items(int stacksize){
		this.stacksize = stacksize;
	}
	
	private int stacksize = 40;
	
	public boolean tool(){
		return tool;
	}
	
	public float power(){
		return power;
	}
	
	public boolean breaks(Material mat){
		return true;
	}
	
	public int getMaxStackSize(){
		return stacksize;
	}
}
