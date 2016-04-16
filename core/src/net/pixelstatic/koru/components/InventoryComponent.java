package net.pixelstatic.koru.components;

import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;

import com.badlogic.ashley.core.Component;

public class InventoryComponent implements Component{
	public ItemStack[][] inventory;
	
	public InventoryComponent(int width, int height){
		inventory = new ItemStack[width][height];
	}
	
	public boolean addItem(ItemStack item){
		ItemStack stack = new ItemStack(item);
		for(int x = 0; x < inventory.length; x ++){
			for(int y = 0; y < inventory[x].length; y ++){
				if(inventory[x][y] == null){
					inventory[x][y] = stack;
					return true;
				}else if(inventory[x][y].item == stack.item){
					if(inventory[x][y].amount + stack.amount <= stack.item.getMaxStackSize()){
						inventory[x][y].amount += stack.amount;
						return true;
					}else{
						int overflow = inventory[x][y].amount + stack.amount - stack.item.getMaxStackSize();
						stack.amount = overflow;
					}
				}
			}
		}
		return false;
	}
	
	public boolean removeItem(ItemStack item){
		ItemStack stack = new ItemStack(item);
		for(int x = 0; x < inventory.length; x ++){
			for(int y = 0; y < inventory[x].length; y ++){
				if(inventory[x][y] == null) continue;
				if(inventory[x][y].item == stack.item){
					if(inventory[x][y].amount >= stack.amount){
						inventory[x][y].amount -= stack.amount;
						if(inventory[x][y].amount <= 0) inventory[x][y] = null;
						return true;
					}else{
						stack.amount -= inventory[x][y].amount;
						inventory[x][y] = null;
					}
				}
			}
		}
		return stack.amount == 0;
	}
	
	public int quantityOf(Item item){
		int sum = 0;
		for(int x = 0; x < inventory.length; x ++){
			for(int y = 0; y < inventory[x].length; y ++){
				if(inventory[x][y] != null && inventory[x][y].item == item){
					sum += inventory[x][y].amount;
				}
			}
		}
		return sum;
	}
		
}
