package io.anuke.koru.entities;

import io.anuke.koru.components.KoruComponent;


public abstract class EntityType{
	
	public abstract ComponentList components();
	
	public void init(KoruEntity entity){}
	
	public void update(KoruEntity entity){}
	
	public boolean unload(){return true;}
	
	public boolean collide(KoruEntity entity, KoruEntity other){return true;}
	
	public void onDeath(KoruEntity entity, KoruEntity killer){
		entity.removeServer();
	}
	
	protected static ComponentList list(KoruComponent... components){
		return new ComponentList(components);
	}
	/*
	player{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new ConnectionComponent(),
			new RenderComponent(new PlayerRenderer()), new HitboxComponent()
			.init(8, 8, 6, 8, 3),
			new VelocityComponent(), new WeaponComponent(),
			new SyncComponent(SyncType.player, new Interpolator()), new InputComponent(), 
			new HealthComponent(), new InventoryComponent(4,6)};
		}
		
		public boolean unload(){
			return false;
		}
	},
	testmob{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(),
			new RenderComponent(new EnemyRenderer()), 
			new HitboxComponent().init(12, 6, 4, 8, 3), new VelocityComponent(),
			new SyncComponent(SyncType.position, new Interpolator()),
			new HealthComponent()};
		}
	},
	projectile{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new ProjectileRenderer()),
					new VelocityComponent().set(0f, 999f), new HitboxComponent(), new ProjectileComponent(),
					new FadeComponent(), new DestroyOnTerrainHitComponent(), new DamageComponent()};
		}

		void initHitbox(KoruEntity entity, HitboxComponent hitbox){
			hitbox.terrainRect().set(0, 0, 4, 2);
			hitbox.entityRect().set(0, 0, 3, 3);
			hitbox.entityhitbox.setCenter(0, -2);
			hitbox.terrainhitbox.setCenter(0, -1);
			hitbox.collideterrain = true;
		}
		
		public boolean collide(KoruEntity entity, KoruEntity other){
			return entity.mapComponent(DamageComponent.class).source != other.getID() && !entity.mapComponent(ProjectileComponent.class).hit.contains(other.getID());
		}
	},
	damageindicator{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new IndicatorRenderer()),
					new ChildComponent(), new TextComponent(), new FadeComponent(20).enableRender()};
		}
	},
	item{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), 
					new RenderComponent(new ItemRenderer()),
					new SyncComponent(SyncType.position),
					new ItemComponent(), new VelocityComponent()};
		}
	},
	particle{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new ParticleRenderer()), new ParticleComponent()};
		}
	},
	blockanimation{
		public Component[] defaultComponents(){
			return new Component[]{new PositionComponent(), new RenderComponent(new BlockAnimationRenderer()), new DataComponent()};
		}
	},
	tile{
		public Component[] defaultComponents(){
			return new Component[]{new TileComponent()};
		}
	};
	
	//TODO make this less clunky?
	final void init(KoruEntity entity){
		InputComponent input = entity.mapComponent(InputComponent.class);
		if(input != null) input.input = new InputHandler(entity);

		HitboxComponent hitbox = entity.mapComponent(HitboxComponent.class);
		if(hitbox != null) initHitbox(entity, hitbox);
	}
	
	public boolean unload(){
		return true;
	}

	public boolean collide(KoruEntity entity, KoruEntity other){
		return true;
	}
	
	public void deathEvent(KoruEntity entity, KoruEntity killer){
		entity.removeServer();
	}

	void initHitbox(KoruEntity entity, HitboxComponent hitbox){
	}

	public Component[] defaultComponents(){
		return new Component[]{};
	}
	*/
}
