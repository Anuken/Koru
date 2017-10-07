package io.anuke.koru.items;

import java.util.EnumSet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;

import io.anuke.koru.input.InputHandler.ClickEvent;
import io.anuke.koru.world.BreakType;
import io.anuke.ucore.util.Strings;

public class Item{
	private static int lastid;
	private static Array<Item> items = new Array<Item>();
	
	private final String name;
	private final EnumSet<ItemType> types;
	private final int id;
	
	protected ObjectIntMap<BreakType> breakSpeeds = new ObjectIntMap<>();
	protected String formalName;
	protected int stackSize = 100;
	
	public static Item getItem(int id){
		return items.get(id);
	}
	
	public Item(String name, ItemType... types){
		this.name = name;
		this.types = EnumSet.of(ItemType.any, types);
		id = lastid ++;
		items.add(this);
		formalName = Strings.capitalize(name);
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
	
	public int getBreakSpeed(BreakType type){
		return breakSpeeds.get(type, 0);
	}
	
	public String formalName(){
		return formalName;
	}
	
	public int id(){
		return id;
	}
	
	public void onClickEvent(ClickEvent event){}
}
