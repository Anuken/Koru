package net.pixelstatic.koru.entities;

import net.pixelstatic.koru.components.*;
import net.pixelstatic.koru.network.IServer;
import net.pixelstatic.koru.network.InputHandler;
import net.pixelstatic.koru.network.Interpolator;
import net.pixelstatic.koru.renderers.*;
import net.pixelstatic.koru.systems.SyncSystem.SyncType;

import com.badlogic.ashley.core.Component;

public enum EntityType{
	player{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new ConnectionComponent(),
			new RenderComponent(new PlayerRenderer()), new HitboxComponent(),
			new SyncComponent(SyncType.position, new Interpolator()), new InputComponent(), 
			new HealthComponent(), new InventoryComponent(4,4)};
		}

		void initHitbox(KoruEntity entity, HitboxComponent hitbox){

			hitbox.terrainRect().set(0, 0, 4, 2);

			hitbox.entityRect().set(0, 0, 4, 5);
			hitbox.alignBottom();
			hitbox.height = 8;
		}
		
		public void deathEvent(KoruEntity entity, KoruEntity killer){
		
		}
	},
	projectile{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new ProjectileRenderer()),
					new VelocityComponent().setDrag(0f), new HitboxComponent(), new ProjectileComponent(),
					new FadeComponent(), new DestroyOnTerrainHitComponent(), new DamageComponent()};
		}

		void initHitbox(KoruEntity entity, HitboxComponent hitbox){
			hitbox.terrainRect().set(0, 0, 4, 2);
			hitbox.entityRect().set(0, 0, 3, 3);
			hitbox.entityhitbox.setCenter(0, 4);
			hitbox.terrainhitbox.setCenter(0, 1);
			hitbox.collideterrain = true;
		}
		
		public boolean collide(KoruEntity entity, KoruEntity other){
			return entity.mapComponent(DamageComponent.class).source != other.getID();
		}
	},
	damageindicator{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new IndicatorRenderer()),
					new ChildComponent(), new TextComponent(), new FadeComponent(20).enableRender()};
		}
	},
	particle{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new ParticleRenderer()), new ParticleComponent()};
		}
	},
	tile{
		public Component[] defaultComponents(){
			return new Component[]{new TileComponent()};
		}
	};

	final void init(KoruEntity entity){
		InputComponent input = entity.mapComponent(InputComponent.class);
		if(input != null && IServer.active()) input.input = new InputHandler(entity);

		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		if(hitbox != null) initHitbox(entity, hitbox);
		
		
	}

	public boolean collide(KoruEntity entity, KoruEntity other){
		return true;
	}

	//public void collisionEvent(KoruEntity entity, KoruEntity other){
	//}
	
	public void deathEvent(KoruEntity entity, KoruEntity killer){
		entity.removeSelfServer();
	}

	void initHitbox(KoruEntity entity, HitboxComponent hitbox){
	}


	public Component[] defaultComponents(){
		return new Component[]{};
	}
}
