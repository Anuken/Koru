package net.pixelstatic.koru.entities;

import net.pixelstatic.koru.behaviors.MoveTowardBehavior;
import net.pixelstatic.koru.behaviors.TargetBehavior;
import net.pixelstatic.koru.components.*;
import net.pixelstatic.koru.network.Interpolator;
import net.pixelstatic.koru.renderers.*;
import net.pixelstatic.koru.server.InputHandler;
import net.pixelstatic.koru.server.KoruServer;
import net.pixelstatic.koru.systems.SyncSystem.SyncType;

import com.badlogic.ashley.core.Component;

public enum EntityType{
	player{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new ConnectionComponent(), new RenderComponent(new PlayerRenderer()),
					new HitboxComponent(), new SyncComponent(SyncType.position), new InputComponent(),
					new HealthComponent()};
		}

		void initHitbox(KoruEntity entity, HitboxComponent hitbox){
			
			hitbox.terrainRect().set(0, 0, 4, 2);

			hitbox.entityRect().set(0, 0, 4, 5);
			hitbox.alignBottom();
		}
	},
	projectile{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new ProjectileRenderer()), new VelocityComponent().setDrag(0f),
					new HitboxComponent(), new ProjectileComponent(), new FadeComponent(),
					new DestroyOnTerrainHitComponent()};
		}

		void initHitbox(KoruEntity entity, HitboxComponent hitbox){
			hitbox.terrainRect().set(0, 0, 4, 2);
			hitbox.entityRect().set(0,0,4,4);
			hitbox.entityhitbox.setCenter(0, 4);
			hitbox.terrainhitbox.setCenter(0, 1);
			hitbox.collideterrain = true;
		}
	},
	genericmonster{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new MonsterRenderer()), new HitboxComponent(),
					new SyncComponent(SyncType.position, new Interpolator()), new BehaviorComponent(), new HealthComponent(),
					new VelocityComponent()};
		}

		void initHitbox(KoruEntity entity, HitboxComponent hitbox){
			hitbox.terrainRect().set(0, 0, 4, 2);
			hitbox.collideterrain = true;

			hitbox.entityRect().set(0, 0, 6, 5);
			hitbox.alignBottom();
		}

		void initBehavior(KoruEntity entity, BehaviorComponent behavior){
			behavior.addBehavior(TargetBehavior.class).setRange(100f).setType(EntityType.player);
			behavior.addBehavior(MoveTowardBehavior.class);
		}
		
		public boolean collide(EntityType other){
			return other == EntityType.projectile;
		}
	},
	damageindicator{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new IndicatorRenderer()) , new ChildComponent(), new FadeComponent(20).enableRender()};
		}
	};

	final void init(KoruEntity entity){
		InputComponent input = entity.mapComponent(InputComponent.class);
		if(input != null && KoruServer.active) input.input = new InputHandler(entity);

		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		if(hitbox != null) initHitbox(entity, hitbox);

		BehaviorComponent behavior = entity.mapComponent(BehaviorComponent.class);
		if(behavior != null) initBehavior(entity, behavior);

	}
	
	public boolean collide(EntityType other){
		return true;
	}
	
	//public void collisionEvent(KoruEntity entity, KoruEntity other){
	//}

	void initHitbox(KoruEntity entity, HitboxComponent hitbox){
	}

	void initBehavior(KoruEntity entity, BehaviorComponent component){
	}

	public Component[] defaultComponents(){
		return new Component[]{};
	}
}
