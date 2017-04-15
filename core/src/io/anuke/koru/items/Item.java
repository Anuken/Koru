package io.anuke.koru.items;

import java.util.EnumSet;

import com.badlogic.gdx.utils.Array;

import io.anuke.koru.input.InputHandler.ClickEvent;
import io.anuke.koru.world.BreakType;

public class Item{
	private static int lastid;
	private static Array<Item> items = new Array<Item>();
	
	private final String name;
	private final EnumSet<ItemType> types;
	private final int id;
	private int stackSize = 100;
	
	public static Item getItem(int id){
		return items.get(id);
	}
	
	public Item(String name, ItemType... types){
		this.name = name;
		this.types = EnumSet.of(ItemType.any, types);
		id = lastid ++;
		items.add(this);
	}
	
	public int getMaxStackSize(){
		return stackSize;
	}
	
	public EnumSet<ItemType> types(){
		return types;
	}
	
	public boolean isType(ItemType type){
		return types.contains(type);
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
	
	public WeaponType weaponType(){
		return WeaponType.sword;
	}
	
	public float getBreakSpeed(BreakType type){
		return 0;
	}
	
	public void onClickEvent(ClickEvent event){}
}
