package io.anuke.koru.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;

import io.anuke.koru.Koru;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.components.WeaponComponent;
import io.anuke.koru.entities.Effects;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.items.*;
import io.anuke.koru.modules.World;
import io.anuke.koru.network.IServer;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;
import io.anuke.koru.world.materials.MaterialTypes;
import io.anuke.koru.world.materials.Materials;

//TODO make this less messy
public class InputHandler{
	public final static float reach = 75;
	public float mouseangle;
	private ObjectMap<InputType, Boolean> keys = new ObjectMap<InputType, Boolean>();
	KoruEntity entity;
	int blockx, blocky;
	float blockhold;
	float cooldown = 0;

	public InputHandler(KoruEntity entity) {
		this.entity = entity;
	}

	public void update(float delta){
		
		
		//weapon updating
		ItemStack stack = entity.getComponent(InventoryComponent.class).hotbarStack();
		
		/*
		if(stack != null && stack.item.type()== ItemType.weapon){
			stack.item.weaponType().setData(entity, stack, this, entity.get(WeaponComponent.class));
			stack.item.weaponType().update(delta);
		}
		*/
		// block breaking
		if(key(InputType.leftclick_down)){
			Tile tile = IServer.instance().getWorld().getTile(blockx, blocky);
			Material block = tile.block();
			Material floor = tile.topTile();
			
			Material select = null;
			
			if(Vector2.dst(World.world(blockx), World.world(blocky), entity.getX(), entity.getY()) < reach
					&& stack != null && stack.item.isType(ItemType.tool)){
				
				if(stack.breaks(block.breakType()) && block.isBreakable()){
					select = block;
				}else if (stack.breaks(floor.breakType()) && floor.isBreakable() && tile.canRemoveTile()){
					select = floor;
				}else{
					blockhold = 0;
					return;
				}
				
				
				blockhold += delta * stack.item.getBreakSpeed(select.breakType());

				if((int) (blockhold) % 20 == 1)
					Effects.blockParticle(World.world(blockx), select.getType() == MaterialTypes.block
							? World.world(blocky) - 6 : World.world(blocky), select);

				if(blockhold >= select.breaktime()){
					Effects.blockBreakParticle(World.world(blockx), World.world(blocky) - 1, select);

					if(select.getType() == MaterialTypes.tree)
						Effects.block(select, blockx, blocky);

					// entity.getComponent(InventoryComponent.class).addItems(tile.block().getDrops());
					// entity.getComponent(InventoryComponent.class).sendUpdate(entity);

					Effects.drops(World.world(blockx), World.world(blocky), select.getDrops());
					
					if(select == floor)
						tile.removeTile();
					else
						tile.setBlockMaterial(Materials.air);

					// schedule this later.

					IServer.instance().getWorld().updateLater(blockx, blocky);

				}

			}else{
				blockhold = 0;
			}

		}else{
			blockhold = 0;
		}
		
	}

	public void inputEvent(InputType type, Object... data){
		inputKey(type, data);
		if(type.name().contains("up")){
			keys.put(InputType.values()[type.ordinal() - 1], false);
		}else if(type.name().contains("down")){
			keys.put(type, true);
		}
	}

	private boolean key(InputType type){
		return keys.get(type, false);
	}

	private void tileDownEvent(){
		// block place check
		ItemStack stack = entity.getComponent(InventoryComponent.class).hotbarStack();

		if(stack == null)
			return;

		Item item = stack.item;
		
		
		if(item.isType(ItemType.placer)){

			Tile tile = World.instance().getTile(blockx, blocky);

			InventoryComponent inv = entity.getComponent(InventoryComponent.class);

			BlockRecipe recipe = null;
			
			if(inv.recipe != -1) recipe = BlockRecipe.getRecipe(inv.recipe);
			
			if(Vector2.dst(World.world(blockx), World.world(blocky), entity.getX(), entity.getY()) < reach
					&& inv.recipe != -1 && inv.hasAll(recipe.requirements())
					&& World.isPlaceable(recipe.result(), tile)){

				if(recipe.result().getType().tile()){
					tile.addTile(recipe.result());
				}else{
					tile.setBlockMaterial(recipe.result());
				}
				
				World.instance().updateTile(blockx, blocky);

				inv.removeAll(recipe.requirements());
				inv.sendUpdate(entity);
			}
		}
	}
	
	private void onInteract(){
		Koru.log("handled interacting");
		Tile tile = World.instance().getTile(blockx, blocky);
		
		if(tile.block().interactable()){
			tile.block().onInteract(tile, blockx, blocky, entity);
			
			World.instance().updateTile(blockx, blocky);
		}
	}

	private void rawClick(boolean left){
		ItemStack stack = entity.getComponent(InventoryComponent.class).hotbarStack();

		if(stack == null)
			return;
		
		if(stack.isType(ItemType.weapon)){
			stack.getWeaponType().setData(entity, stack, this, entity.get(WeaponComponent.class));
			stack.getWeaponType().clicked(left);
		}
	}

	private void inputKey(InputType type, Object... data){
		if(type == InputType.leftclick_down){
			blockx = (int) data[0];
			blocky = (int) data[1];
			click(true);
			rawClick(true);
		}else if(type == InputType.rightclick_down){
			rawClick(false);
		}else if(type == InputType.block_moved){
			click(false);

			blockhold = 0;
			blockx = (int) data[0];
			blocky = (int) data[1];

			click(true);
		}else if(type == InputType.interact){
			blockx = (int) data[0];
			blocky = (int) data[1];
			onInteract();
		}
	}
	


	private void click(boolean down){

		if(down)
			tileDownEvent();

		// fire block click event
		InventoryComponent inv = entity.inventory();
		int slot = inv.hotbar;
		ItemStack stack = inv.inventory[slot][0];
		if(stack == null)
			return;
		Tile tile = IServer.instance().getWorld().getTile(blockx, blocky);

		ClickEvent event = Pools.get(ClickEvent.class).obtain().set(down, blockx, blocky, tile, inv);
		stack.item.onClickEvent(event);
		event.free();
	}

	public static class ClickEvent{
		public int x, y;
		public Tile tile;
		public InventoryComponent component;
		public ItemStack stack;
		public boolean down;

		public ClickEvent set(boolean click, int x, int y, Tile tile, InventoryComponent component){
			this.down = click;
			this.x = x;
			this.y = y;
			this.tile = tile;
			this.component = component;
			return this;
		}

		public void free(){
			Pools.free(this);
		}
	}

	public static enum ClickType{
		up, down;
	}
}
