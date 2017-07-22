package io.anuke.koru.traits;

import java.util.function.Consumer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.InventoryUpdatePacket;
import io.anuke.koru.network.packets.SlotChangePacket;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;

//TODO fix
public class InventoryTrait extends Trait{
	private static final ObjectMap<Item, Integer> temp = new ObjectMap<Item, Integer>();
	private static final ObjectSet<Item> tempset = new ObjectSet<Item>();
	private static final Array<ItemStack> tempArray = new Array<ItemStack>();

	public ItemStack[] inventory;
	public ItemStack selected;
	public transient int hotbar;
	public int recipe = -1;

	public InventoryTrait(int size) {
		inventory = new ItemStack[size];
	}

	private InventoryTrait() {
	}

	public void clickSlot(int i){
		ItemStack stack = inventory[i];
		if(selected == null){
			if(stack != null){
				inventory[i] = null;
				selected = stack;
			}
		}else{
			if(stack != null){
				if(stack.item != selected.item){
					inventory[i] = selected;
					selected = stack;
				}else{
					int max = inventory[i].item.getMaxStackSize();
					int addable = max - inventory[i].amount;
					int added = Math.min(addable, selected.amount);

					inventory[i].amount += added;

					selected.amount -= added;

					if(selected.amount == 0)
						selected = null;
				}
			}else{
				inventory[i] = selected;
				selected = null;
			}
		}
	}

	public ItemStack hotbarStack(){
		return inventory[hotbar];
	}

	/** Server-side only. */
	public void sendUpdate(Spark entity){
		InventoryUpdatePacket update = new InventoryUpdatePacket();
		update.selected = selected;
		update.stacks = inventory;
		IServer.instance().send(entity.get(ConnectionTrait.class).connectionID, update, false);
	}

	/** Server-side only. */
	public void sendHotbarUpdate(Spark entity){
		SlotChangePacket update = new SlotChangePacket();
		update.stack = hotbarStack();
		IServer.instance().sendToAllExcept(entity.get(ConnectionTrait.class).connectionID, update);
	}

	/** Server-side only. */
	public void sendHotbarUpdate(Spark entity, int to){
		SlotChangePacket update = new SlotChangePacket();
		update.stack = hotbarStack();
		IServer.instance().send(to, update, false);
	}

	public void set(ItemStack[] stacks, ItemStack selected){
		this.selected = selected;
		
		for(int i = 0; i < inventory.length; i++){
			inventory[i] = stacks[i];
		}

	}

	public void addItems(Iterable<ItemStack> items){
		for(ItemStack item : items)
			addItem(item);
	}

	public void addItems(ItemStack[] items){
		for(ItemStack item : items)
			addItem(item);
	}

	/**
	 * Adds the item to the inventory.
	 * 
	 * @param item
	 *            - itemstack you wish to remove
	 * @return what remains of the stack after adding it. May be a stack with
	 *         quantity 0.
	 */
	public ItemStack addItem(ItemStack item){
		if(item.amount == 0)
			return item;

		ItemStack stack = new ItemStack(item);

		//attempt to stack on top first
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] == null)
				continue;

			if(inventory[i].item == stack.item){
				if(inventory[i].amount + stack.amount <= stack.item.getMaxStackSize()){
					inventory[i].amount += stack.amount;
					stack.amount = 0;
					return stack;
				}else{
					int overflow = inventory[i].amount + stack.amount - stack.item.getMaxStackSize();
					inventory[i].amount = stack.item.getMaxStackSize();
					stack.amount = overflow;
				}
			}

		}

		//if that doesn't work, put it in a new slot
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] == null){
				inventory[i] = stack.clone();
				stack.amount = 0;
				return stack;
			}
		}

		return stack;
	}

	public int size(){
		return inventory.length;
	}

	public boolean full(){
		return usedSlots() == size();
	}

	public int usedSlots(){
		int slots = 0;
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] != null)
				slots++;
		}

		return slots;
	}

	public void clear(){
		for(int i = 0; i < inventory.length; i++){

			inventory[i] = null;
		}

	}

	public int getAmountOf(Item item){
		int amount = 0;
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] != null && inventory[i].item == item){
				amount += inventory[i].amount;
			}

		}
		return amount;
	}

	public ObjectMap<Item, Integer> merge(InventoryTrait component){
		return merge(component.inventory);
	}

	/**
	 * Adds the whole itemstack array to this inventory, if possible.
	 * 
	 * @return an objectmap of items removed, with the key being the type and
	 *         the value being the amount removed.
	 */
	public ObjectMap<Item, Integer> merge(ItemStack[] items){
		temp.clear();
		for(int i = 0; i < items.length; i++){
			if(items[i] == null)
				continue;
			ItemStack add = this.addItem(items[i]);
			if(!temp.containsKey(add.item)){
				temp.put(add.item, items[i].amount - add.amount);
			}else{
				temp.put(add.item, temp.get(add.item) + items[i].amount - add.amount);
			}
			if(add.amount != 0){
				items[i] = add;
			}else{
				items[i] = null;
			}

		}
		return temp;
	}

	public boolean removeItem(ItemStack item){
		ItemStack stack = new ItemStack(item);
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] == null)
				continue;
			if(inventory[i].item == stack.item){
				if(inventory[i].amount >= stack.amount){
					inventory[i].amount -= stack.amount;
					if(inventory[i].amount <= 0)
						inventory[i] = null;
					return true;
				}else{
					stack.amount -= inventory[i].amount;
					inventory[i] = null;
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

	public boolean removeAll(ItemStack[] items){
		boolean removed = true;
		for(ItemStack stack : items){
			removed = removed && removeItem(stack);
		}
		return removed;
	}

	public boolean hasAll(Iterable<ItemStack> items){
		for(ItemStack stack : items){
			if(!hasItem(stack))
				return false;
		}
		return true;
	}

	public boolean hasAll(ItemStack[] items){
		for(ItemStack stack : items){
			if(!hasItem(stack))
				return false;
		}
		return true;
	}

	public boolean hasItem(ItemStack item){
		ItemStack stack = new ItemStack(item);
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] == null)
				continue;
			if(inventory[i].item == stack.item){
				if(inventory[i].amount >= stack.amount){
					return true;
				}else{
					stack.amount -= inventory[i].amount;
				}
			}

		}
		return stack.amount == 0;
	}

	public int quantityOf(Item item){
		int sum = 0;
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] != null && inventory[i].item == item){
				sum += inventory[i].amount;
			}

		}
		return sum;
	}

	/** Populates the given array with itemstacks, no duplicates. */
	public Array<ItemStack> asArray(){
		tempArray.clear();

		forEach(stack -> {
			for(ItemStack other : tempArray){
				if(other.item == stack.item){
					other.amount += stack.amount;
					return;
				}
			}
			tempArray.add(stack.clone());
		});

		return tempArray;
	}

	public void forEach(Consumer<ItemStack> cons){
		for(int i = 0; i < inventory.length; i++){

			if(inventory[i] != null)
				cons.accept(inventory[i]);

		}
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < inventory.length; i++){

			builder.append("[" + inventory[i] + "]");

			builder.append("\n");
		}
		return builder.toString();
	}

}
