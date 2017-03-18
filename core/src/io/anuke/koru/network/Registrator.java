package io.anuke.koru.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import io.anuke.aabb.Collider;
import io.anuke.koru.components.*;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.ProjectileType;
import io.anuke.koru.entities.types.*;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.Items;
import io.anuke.koru.network.packets.*;
import io.anuke.koru.network.syncing.SyncData;
import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.renderers.AnimationType;
import io.anuke.koru.systems.SyncSystem.SyncType;
import io.anuke.koru.utils.InputType;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.Material;

public class Registrator{
	
	public static void register(Kryo k){
		k.register(ConnectPacket.class);
		k.register(PositionPacket.class);
		k.register(DataPacket.class);
		k.register(WorldUpdatePacket.class);
		k.register(EntityRemovePacket.class);
		k.register(ChunkRequestPacket.class);
		k.register(ChunkPacket.class);
		k.register(InputPacket.class);
		k.register(TileUpdatePacket.class);
		k.register(BlockInputPacket.class);
		k.register(StoreItemPacket.class);
		k.register(ChatPacket.class);
		k.register(BitmapDataPacket.class);
		k.register(BitmapDataPacket.Header.class);
		k.register(MaterialRequestPacket.class);
		k.register(InventoryUpdatePacket.class);
		k.register(InventoryClickPacket.class);
		k.register(SlotChangePacket.class);
		k.register(RecipeSelectPacket.class);
		k.register(EntityRequestPacket.class);
		k.register(AnimationPacket.class);

		k.register(EntityType.class);
		k.register(AnimationType.class);
		k.register(EntityWrapper.class);
		k.register(PositionComponent.class);
		k.register(ProjectileComponent.class);
		k.register(FadeComponent.class);
		k.register(ConnectionComponent.class);
		k.register(ChildComponent.class);
		k.register(TextComponent.class);
		k.register(InventoryComponent.class);
		k.register(ParticleComponent.class);
		k.register(MaterialComponent.class);
		k.register(ItemComponent.class);
		k.register(VelocityComponent.class);
		k.register(ColliderComponent.class);
		k.register(HealthComponent.class);

		k.register(Collider.class);
		k.register(ProjectileType.class);
		k.register(SyncData.class);
		
		k.register(Player.class);
		k.register(ItemDrop.class);
		k.register(Particle.class);
		k.register(Projectile.class);
		k.register(TestEntity.class);
		k.register(DamageIndicator.class);
		k.register(BlockAnimation.class);
		
		k.register(Chunk.class);
		k.register(Tile.class, new TileSerializer());
		k.register(Tile[].class);
		k.register(Tile[][].class);
		k.register(Tile[][][].class);
		k.register(Material.class, new MaterialsSerializer());
		k.register(ItemStack.class);
		k.register(ItemStack[].class);
		k.register(ItemStack[][].class);
		k.register(Items.class);
		
		k.register(InputType.class);
		k.register(SyncType.class);
		k.register(Component.class);
		k.register(Component[].class);
		k.register(Object[].class);
		k.register(Bits.class);
		k.register(Color.class);
		k.register(Vector2.class);
		k.register(ArrayList.class);
		k.register(ObjectMap.class);
		k.register(ConcurrentHashMap.class);
		k.register(ObjectMap.Keys.class);
		k.register(HashMap.class);
		k.register(byte[].class);
		k.register(int[].class);
		k.register(Class.class);
		

		k.register(KoruEntity.class, new EntitySerializer());
	}
	
	public static class TileSerializer extends Serializer<Tile>{
		@Override
		public Tile read(Kryo k, Input i, Class<Tile> c){
			Tile tile = Tile.unloadedTile();
			
			
			tile.layers = new int[]{k.readObject(i, int.class)};
			tile.blockid = k.readObject(i, int.class);
			tile.light = k.readObject(i, byte.class);
			tile.top = k.readObject(i, byte.class);
			return tile;
		}

		@Override
		public void write(Kryo k, Output o, Tile t){
			k.writeObject(o, t.tileid());
			k.writeObject(o, t.blockid);
			k.writeObject(o, t.light);
			k.writeObject(o, t.top);
		}
	}
	
	public static class MaterialsSerializer extends Serializer<Material>{
		@Override
		public Material read(Kryo k, Input i, Class<Material> c){
			return (Material)Material.getMaterial(k.readObject(i, int.class));
		}

		@Override
		public void write(Kryo k, Output o, Material m){
			k.writeObject(o, m.id());
		}
	}

	static class EntitySerializer extends Serializer<KoruEntity>{

		@Override
		public KoruEntity read(Kryo k, Input input, Class<KoruEntity> c){
			return k.readObject(input, EntityWrapper.class).getEntity();
		}

		@Override
		public void write(Kryo k, Output output, KoruEntity entity){
			k.writeObject(output, new EntityWrapper(entity));
		}

	}
	
	static Array<Component> toadd = new Array<Component>();

	static class EntityWrapper{
		HashMap<Class<?>, Component> components = new HashMap<Class<?>, Component>();
		long id;
		Class<? extends EntityType> type;

		private EntityWrapper(){}

		public EntityWrapper(KoruEntity entity){
			
			this.id = entity.getID();
			this.type = entity.getTypeClass();
			for(Component component : entity.getComponents()){
			
				if(ClassReflection.getAnnotation(component.getClass(), Synced.class) != null){
					components.put(component.getClass(), component);
				}
			}
		}

		public KoruEntity getEntity(){
			KoruEntity entity = KoruEntity.loadedEntity(type, id);
			ImmutableArray<Component> icomponents = entity.getComponents();
			
			for(Component component : icomponents){
				
				if(ClassReflection.getAnnotation(component.getClass(), Synced.class) != null){
					toadd.add(components.get(component.getClass()));
				}
			}
			
			for(Component component : toadd){
				entity.add(component);
			}
			
			entity.getType().init(entity);
			
			toadd.clear();
			return entity;

		}
	}
}
