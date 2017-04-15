package io.anuke.koru.items;

import io.anuke.koru.world.BreakType;

public class ItemStack implements Cloneable{
	public Item item;
	public int amount;
	public ItemData data;
	
	public ItemStack(Item item, int amount){
		this.item = item;
		this.amount = amount;
	}
	
	public ItemStack(Item item){
		this(item, 1);
	}
	
	public ItemStack(ItemStack stack){
		this(stack.item, stack.amount);
	}
	
	public ItemStack(){
		
	}
	
	public boolean isType(ItemType type){
		return item.isType(type);
	}
	
	public WeaponType getWeaponType(){
		return item.weaponType();
	}
	
	public float getBreakSpeed(BreakType type){
		return item.getBreakSpeed(type);
	}
	
	public boolean breaks(BreakType type){
		return getBreakSpeed(type) > 0.0001f;
	}
	
	public <T extends ItemData> T data(){
		return (T)data;
	}
	
	public void set(Item item, int amount){
		this.item = item;
		this.amount = amount;
	}
	
	public ItemStack clone(){
		return new ItemStack(this);
	}
	
	public String toString(){
		return item.name() + "x" + amount;
	}
}
