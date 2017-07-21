package io.anuke.koru.items;

import io.anuke.koru.components.WeaponComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.ProjectileType;
import io.anuke.koru.input.InputHandler;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.AnimationPacket;
import io.anuke.koru.renderers.AnimationType;
import io.anuke.koru.systems.SyncSystem;

public enum WeaponType{
	sword{
		public void clicked(boolean left){
			if(weapon.cooldown <= 0){
				KoruEntity projectile = ProjectileType.createProjectile(entity.getID(), ProjectileType.slash,
						input.mouseangle, 10);
				projectile.pos().set(entity.pos());

				projectile.add().send();

				playAnimation(AnimationType.attack);
				weapon.cooldown = 15;
			}
		}
	};
	protected KoruEntity entity;
	protected ItemStack stack;
	protected InputHandler input;
	protected WeaponComponent weapon;

	public void setData(KoruEntity entity, ItemStack stack, InputHandler input, WeaponComponent weapon){
		this.entity = entity;
		this.stack = stack;
		this.input = input;
		this.weapon = weapon;
	}

	public void clicked(boolean left){

	}

	public void update(float delta){
		weapon.cooldown -= delta;
	}

	void playAnimation(AnimationType type){
		AnimationPacket packet = new AnimationPacket();
		packet.type = type;
		packet.player = entity.getID();
		IServer.instance().sendToAllIn(packet, entity.getX(), entity.getY(), SyncSystem.syncrange);
	}
}
