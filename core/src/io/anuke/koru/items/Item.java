package io.anuke.koru.items;

import io.anuke.koru.input.InputHandler.ClickEvent;
import io.anuke.koru.world.materials.Material;

public class Item{
	private final String name;
	private final ItemType type;
	private int stackSize = 100;
	
	public Item(String name, ItemType type){
		this.name = name;
		this.type = type;
	}
	
	public int getMaxStackSize(){
		return stackSize;
	}
	
	public ItemType type(){
		return type;
	}
	
	public boolean breaks(Material mat){
		return false;
	}
	
	public String name(){
		return name;
	}
	
	public String formalName(){
		return name().substring(0, 1).toUpperCase() + name().substring(1);
	}
	
	public void onClickEvent(ClickEvent event){}
}
