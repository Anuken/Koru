package io.anuke.koru.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;

import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.entities.Effects;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.ItemType;
import io.anuke.koru.items.Recipes;
import io.anuke.koru.modules.World;
import io.anuke.koru.utils.InputType;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.MaterialType;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.Tile;

public class InputHandler{
	public float mouseangle;
	private ObjectMap<InputType, Boolean> keys = new ObjectMap<InputType, Boolean>();
	KoruEntity entity;
	int blockx, blocky;
	float blockhold;

	public InputHandler(KoruEntity entity) {
		this.entity = entity;
	}

	public void update(float delta){
		//block breaking
		if(key(InputType.leftclick_down)){
			ItemStack stack = entity.getComponent(InventoryComponent.class).hotbarStack();
			Tile tile = IServer.instance().getWorld().tile(blockx, blocky);
			
			if(stack != null && stack.item.type() == ItemType.tool && stack.item.breaks(tile.block()) && tile.block().breakable()){
				blockhold += delta * stack.item.power();
				
				
				if((int)(blockhold) % 20 == 1)
				Effects.blockParticle(World.world(blockx), World.world(blocky), tile.block());
				
				if(blockhold >= tile.block().breaktime()){
					Effects.blockBreakParticle(World.world(blockx), World.world(blocky)-1, tile.block());
					
					if(tile.block().getType() == MaterialType.tree)
					Effects.block(tile.block(), blockx, blocky);
					
					//entity.getComponent(InventoryComponent.class).addItems(tile.block().getDrops());
					//entity.getComponent(InventoryComponent.class).sendUpdate(entity);
					
					Effects.drops(World.world(blockx), World.world(blocky), tile.block().getDrops());
					
					tile.setBlockMaterial(Materials.air);
					
					//schedule this later.
					
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
	
	private void mouseDownEvent(){
		//block place check
		ItemStack stack = entity.getComponent(InventoryComponent.class).hotbarStack();
		Tile tile = IServer.instance().getWorld().tile(blockx, blocky);
		
		if(stack != null && stack.item.type() == ItemType.hammer){
			InventoryComponent inv = entity.getComponent(InventoryComponent.class);
			
			if(inv.recipe != -1 && inv.hasAll(Recipes.values()[inv.recipe].requirements()) && Material.isPlaceable(Recipes.values()[inv.recipe].result(), tile)){
				
				tile.setMaterial(Recipes.values()[inv.recipe].result());
				IServer.instance().getWorld().updateTile(blockx, blocky);
				
				inv.removeAll(Recipes.values()[inv.recipe].requirements());
				inv.sendUpdate(entity);
			}
		}
	}

	private void inputKey(InputType type, Object... data){
		if(type == InputType.leftclick_down){
			blockx = (int) data[0];
			blocky = (int) data[1];
			click(true);
			
		}else if(type == InputType.block_moved){
			click(false);

			blockhold = 0;
			blockx = (int) data[0];
			blocky = (int) data[1];

			click(true);
		}else if(type == InputType.r){
			Effects.particle(entity, Color.BLUE);
		}
	}

	private void click(boolean down){
		
		if(down)
			mouseDownEvent();
		
		//fire block click event
		InventoryComponent inv = entity.mapComponent(InventoryComponent.class);
		int slot = inv.hotbar;
		ItemStack stack = inv.inventory[slot][0];
		if(stack == null)
			return;
		Tile tile = IServer.instance().getWorld().getTile(blockx, blocky);

		ClickEvent event = Pools.get(ClickEvent.class).obtain().set(down, blockx, blocky, tile, inv);
		stack.item.clickEvent(event);
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
