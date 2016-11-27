package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.InventoryUpdatePacket;

public class InventoryComponent implements Component{
	private static final ObjectMap<Item, Integer> temp = new ObjectMap<Item, Integer>();
	public ItemStack[][] inventory;
	public ItemStack selected;
	public transient int hotbar;

	public InventoryComponent(int width, int height){
		inventory = new ItemStack[width][height];
	}
	
	@SuppressWarnings("unused")
	private InventoryComponent(){}
	
	public void clickSlot(int x, int y){
		ItemStack stack = inventory[x][y];
		if(selected == null){
			if(stack != null){
				inventory[x][y] = null;
				selected = stack;
			}
		}else{
			if(stack != null){
				inventory[x][y] = selected;
				selected = stack;
			}else{
				inventory[x][y] = selected;
				selected = null;
			}
		}
	}
	
	public ItemStack hotbarStack(){
		return inventory[hotbar][0];
	}
	
	/**Server-side only.*/
	public void sendUpdate(KoruEntity entity){
		InventoryUpdatePacket update = new InventoryUpdatePacket();
		update.selected = selected;
		update.stacks = inventory;
		IServer.instance().sendTCP(entity.mapComponent(ConnectionComponent.class).connectionID, update);
	}
	
	public void set(ItemStack[][] stacks, ItemStack selected){
		this.selected = selected;
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
				inventory[x][y] = stacks[x][y];
			}
		}
	}

	public void addItems(Iterable<ItemStack> items){
		for(ItemStack item : items)
			addItem(item);
	}
	/**
	 * Adds the item to the inventory.
	 * @param item - itemstack you wish to remove
	 * @return what remains of the stack after adding it. May be a stack with quantity 0.
	 */
	public ItemStack addItem(ItemStack item){
		if(item.amount == 0) return item;
		ItemStack stack = new ItemStack(item);
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
				if(inventory[x][y] == null){
					inventory[x][y] = stack.clone();
					stack.amount = 0;
					return stack;
				}else if(inventory[x][y].item == stack.item){
					if(inventory[x][y].amount + stack.amount <= stack.item.getMaxStackSize()){
						inventory[x][y].amount += stack.amount;
						stack.amount = 0;
						return stack;
					}else{
						int overflow = inventory[x][y].amount + stack.amount - stack.item.getMaxStackSize();
						inventory[x][y].amount = stack.item.getMaxStackSize();
						stack.amount = overflow;
					}
				}
			}
		}
		return stack;
	}
	
	public int size(){
		return inventory.length * inventory[0].length;
	}
	
	public boolean full(){
		return usedSlots() == size();
	}
	
	public int usedSlots(){
		int slots = 0;
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
				if(inventory[x][y] != null)
				slots ++;
			}
		}
		return slots;
	}
	
	public void clear(){
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
				inventory[x][y] = null;
			}
		}
	}
	
	public ObjectMap<Item, Integer> merge(InventoryComponent component){
		return merge(component.inventory);
	}
	
	/**
	 * Adds the whole itemstack array to this inventory, if possible.
	 * @return an objectmap of items removed, 
	 * with the key being the type and the value being the amount removed.
	 */
	public ObjectMap<Item, Integer> merge(ItemStack[][] items){
		temp.clear();
		for(int x = 0;x < items.length;x ++){
			for(int y = 0;y < items[x].length;y ++){
				if(items[x][y] == null)continue;
				ItemStack add = this.addItem(items[x][y]);
				if(!temp.containsKey(add.item)){
					temp.put(add.item, items[x][y].amount - add.amount);
				}else{
					temp.put(add.item, temp.get(add.item) + items[x][y].amount - add.amount);
				}
				if(add.amount != 0){
					items[x][y] = add;
				}else{
					items[x][y] = null;
				}
			}
		}
		return temp;
	}

	public boolean removeItem(ItemStack item){
		ItemStack stack = new ItemStack(item);
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
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
	
	public boolean removeAll(Iterable<ItemStack> items){
		boolean removed = true;
		for(ItemStack stack : items){
			removed = removed && removeItem(stack);
		}
		return removed;
	}
	
	public boolean hasAll(Iterable<ItemStack> items){
		for(ItemStack stack : items){
			if(!hasItem(stack)) return false;
		}
		return true;
	}
	
	public boolean hasItem(ItemStack item){
		ItemStack stack = new ItemStack(item);
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
				if(inventory[x][y] == null) continue;
				if(inventory[x][y].item == stack.item){
					if(inventory[x][y].amount >= stack.amount){
						return true;
					}else{
						stack.amount -= inventory[x][y].amount;
					}
				}
			}
		}
		return stack.amount == 0;
	}

	public int quantityOf(Item item){
		int sum = 0;
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
				if(inventory[x][y] != null && inventory[x][y].item == item){
					sum += inventory[x][y].amount;
				}
			}
		}
		return sum;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(int x = 0;x < inventory.length;x ++){
			for(int y = 0;y < inventory[x].length;y ++){
				builder.append("[" + inventory[x][y]+ "]");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

}
