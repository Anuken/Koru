package net.pixelstatic.koru.items;

public class ItemStack{
	public Item item;
	public int amount;
	
	public ItemStack(Item item, int amount){
		this.item = item;
		this.amount = amount;
	}
	
	public ItemStack(ItemStack stack){
		this(stack.item, stack.amount);
	}
	
	public ItemStack(){
		
	}
}
