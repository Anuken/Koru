package io.anuke.koru.network;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import io.anuke.koru.components.ChildComponent;
import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.components.FadeComponent;
import io.anuke.koru.components.InventoryComponent;
import io.anuke.koru.components.ParticleComponent;
import io.anuke.koru.components.PositionComponent;
import io.anuke.koru.components.ProjectileComponent;
import io.anuke.koru.components.TextComponent;
import io.anuke.koru.components.VelocityComponent;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.ProjectileType;
import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.network.SyncBuffer.PositionSyncBuffer;
import io.anuke.koru.network.SyncBuffer.Synced;
import io.anuke.koru.network.packets.BlockInputPacket;
import io.anuke.koru.network.packets.ChatPacket;
import io.anuke.koru.network.packets.ChunkPacket;
import io.anuke.koru.network.packets.ChunkRequestPacket;
import io.anuke.koru.network.packets.ConnectPacket;
import io.anuke.koru.network.packets.DataPacket;
import io.anuke.koru.network.packets.EntityRemovePacket;
import io.anuke.koru.network.packets.InputPacket;
import io.anuke.koru.network.packets.PositionPacket;
import io.anuke.koru.network.packets.StoreItemPacket;
import io.anuke.koru.network.packets.TileUpdatePacket;
import io.anuke.koru.network.packets.WorldUpdatePacket;
import io.anuke.koru.systems.SyncSystem.SyncType;
import io.anuke.koru.utils.InputType;
import io.anuke.koru.world.Chunk;
import io.anuke.koru.world.InventoryTileData;
import io.anuke.koru.world.Material;
import io.anuke.koru.world.Materials;
import io.anuke.koru.world.PinetreeTileData;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.TileData;

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

		k.register(EntityType.class);
		k.register(EntityWrapper.class);
		k.register(PositionComponent.class);
		k.register(VelocityComponent.class);
		k.register(ProjectileComponent.class);
		k.register(FadeComponent.class);
		k.register(ConnectionComponent.class);
		k.register(ChildComponent.class);
		k.register(TextComponent.class);
		k.register(InventoryComponent.class);
		k.register(ParticleComponent.class);

		k.register(ProjectileType.class);
		k.register(SyncBuffer.class);
		k.register(PositionSyncBuffer.class);
		
		k.register(Chunk.class);
		k.register(TileData.class);
		k.register(InventoryTileData.class);
		k.register(PinetreeTileData.class);
		k.register(Tile.class);
		k.register(Tile[].class);
		k.register(Tile[][].class);
		k.register(Tile[][][].class);
		k.register(Materials.class);
		k.register(ItemStack.class);
		k.register(ItemStack[].class);
		k.register(ItemStack[][].class);
		k.register(Item.class);
		
		k.register(InputType.class);
		k.register(SyncType.class);
		k.register(Component.class);
		k.register(Component[].class);
		k.register(Object[].class);
		k.register(Bits.class);
		k.register(Color.class);
		k.register(Vector2.class);
		k.register(ArrayList.class);
		k.register(HashMap.class);
		k.register(Class.class);

		k.register(KoruEntity.class, new EntitySerializer());
	}
	
	public static class MaterialSerializer extends Serializer<Material>{
		@Override
		public Material read(Kryo k, Input i, Class<Material> c){
			return Materials.values()[k.readObject(i, Integer.class)];
		}

		@Override
		public void write(Kryo k, Output o, Material m){
			k.writeObject(o, new Integer(m.id()));
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
		EntityType type;

		@SuppressWarnings("unused")
		private EntityWrapper(){
		}

		public EntityWrapper(KoruEntity entity){
			//components = entity.getComponents().toArray();
			this.id = entity.getID();
			this.type = entity.getType();
			for(Component component : entity.getComponents()){
			
				if(ClassReflection.getAnnotation(component.getClass(), Synced.class) != null){
					//if(component instanceof FadeComponent)Koru.log("sent:" + ((FadeComponent)component).lifetime);
					components.put(component.getClass(), component);
				}
			}
		}

		public KoruEntity getEntity(){
			KoruEntity entity = KoruEntity.loadedEntity(type, id);
			//Koru.log("Entity: " + entity.getID());
			ImmutableArray<Component> icomponents = entity.getComponents();
			for(Component component : icomponents){
				//Koru.log("Iterating component: " + component.getClass().getSimpleName());
				if(ClassReflection.getAnnotation(component.getClass(), Synced.class) != null){
					//if(component instanceof FadeComponent)Koru.log("recieved:" + ((FadeComponent)component).lifetime);
					toadd.add(components.get(component.getClass()));
				//	Koru.log("Adding synced component: " + components.get(component.getClass()).getClass().getSimpleName());
				}
			}
			for(Component component : toadd){
				entity.add(component);
			}
			toadd.clear();
			//for(Object co : components){
				//if(!co.getClass().isAnnotationPresent(Unchanged.class))
			//	entity.add((Component)co);
			//}
			return entity;

		}
	}
}
