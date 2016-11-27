package io.anuke.koru.network;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.entities.Effects;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.utils.InputType;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.World;

public class InputHandler{
	public float mouseangle;
	private HashMap<InputType, Boolean> keys = new HashMap<InputType, Boolean>();
	private float x,y;
	KoruEntity entity;

	public void inputEvent(InputType type, float x, float y){
		this.x = x;
		this.y = y;
		inputKey(type);
		if(type.name().contains("up")){
			keys.put(InputType.values()[type.ordinal() - 1], false);
		}else if(type.name().contains("down")){
			keys.put(type, true);
		}

		/*
		inputKey(type);
		if(type == InputType.rightclick_down){
			rightmousedown = true;
		}else if(type == InputType.rightclick_up){
			rightmousedown = false;
		}
		*/
	}

	private void inputKey(InputType type){
		if(type == InputType.leftclick_down){
		//	KoruEntity projectile = ProjectileType.createProjectile(entity.getID(), ProjectileType.bolt, mouseangle);
		//	projectile.position().set(entity.getX(), entity.getY());
		//	projectile.addSelf().sendSelf();
			//	KoruEntity entity = new KoruEntity(EntityType.testmonster);
			//	entity.position().set(this.entity.getX(), this.entity.getY());
			//	entity.addSelf().sendSelf();
			InventoryComponent inv = entity.mapComponent(InventoryComponent.class);
			int slot = inv.hotbar;
			ItemStack stack = inv.inventory[slot][0];
			if(stack == null) return;
			
			Tile tile = IServer.instance().getWorld().getTile(x, y);
			
			stack.item.clickEvent(inv, stack, World.tile(x), World.tile(y), tile);
		}else if(type == InputType.r){
			Effects.particle(entity, Color.BLUE);
		}
	}

	public InputHandler(KoruEntity entity){
		this.entity = entity;
	}

}
