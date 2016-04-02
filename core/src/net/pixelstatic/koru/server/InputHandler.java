package net.pixelstatic.koru.server;

import java.util.HashMap;

import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.entities.ProjectileType;
import net.pixelstatic.koru.utils.InputType;

public class InputHandler{
	public float mouseangle;
	private HashMap<InputType, Boolean> keys = new HashMap<InputType, Boolean>();
	KoruEntity entity;

	public void inputEvent(InputType type){
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
			KoruEntity projectile = ProjectileType.createProjectile(ProjectileType.bolt, mouseangle);
			projectile.position().set(entity.getX(), entity.getY());
			projectile.addSelf().sendSelf();
		}
	}

	public InputHandler(KoruEntity entity){
		this.entity = entity;
	}

}
