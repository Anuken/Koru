package io.anuke.koru.items;

import com.badlogic.gdx.utils.Array;

import io.anuke.koru.input.InputHandler.ClickEvent;
import io.anuke.koru.world.materials.Material;

public class Item{
	private static int lastid;
	private static Array<Item> items = new Array<Item>();
	
	private final String name;
	private final ItemType type;
	private final int id;
	private int stackSize = 100;
	
	public static Item getItem(int id){
		return items.get(id);
	}
	
	public Item(String name, ItemType type){
		this.name = name;
		this.type = type;
		id = lastid ++;
		items.add(this);
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
	
	public int id(){
		return id;
	}
	
	public void onClickEvent(ClickEvent event){}
}
