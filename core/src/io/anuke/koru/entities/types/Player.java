package io.anuke.koru.entities.types;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import io.anuke.koru.entities.KoruEvents.Unload;
import io.anuke.koru.network.SyncType;
import io.anuke.koru.traits.*;
import io.anuke.koru.traits.DirectionTrait.Direction;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.traits.*;

public class Player extends Prototype{
	
	public Player(){
		event(Unload.class, ()->{
			return false;
		});
	}
/*
	@Override
	public ComponentList components(){
		return list(new PositionComponent(), new ConnectionComponent(),
				new RenderComponent(new PlayerRenderer()), new ColliderComponent(), 
				new WeaponComponent(), new LoadChunksComponent(),
				new SyncComponent(SyncType.player), new InputComponent(), 
				new HealthComponent(), new InventoryComponent(4,6));
	}
	
	public void init(KoruEntity entity){
		entity.get(InputComponent.class).input = new InputHandler(entity);
		entity.collider().setSize(8, 6);
	}
	*/
	
	//TODO
	public boolean removeDeath(){
		return false;
	}
	
	//TODO
	public boolean unload(){
		return false;
	}

	@Override
	public TraitList traits(){
		return new TraitList(
			new PosTrait(),
			new ConnectionTrait(),
			new FacetTrait((trait, spark)->{
				
				trait.draw(l->{
					l.layer = spark.pos().y;
					
					DirectionTrait dir = spark.get(DirectionTrait.class);
					
					Draw.grect("crab-" + (dir.direction == Direction.left || dir.direction == Direction.right ? "s" : dir.direction == Direction.back ? "b" : "f")
						+(dir.walktime > 0 ? "-"+((int)(dir.walktime/7))%3 : ""), 
							spark.pos().x, spark.pos().y, dir.direction == Direction.left ? - 12 : 12, 12);
					
					//TODO fix ugly font size
					if(!spark.get(ConnectionTrait.class).local){
						Resources.font2().setColor(Color.YELLOW);
						
						Resources.font2().getData().setScale(1f);
						Resources.font2().draw(Draw.batch(), spark.get(ConnectionTrait.class).name, (int)spark.pos().x, (int)spark.pos().y + 18, 0, Align.center, false);
					
						Resources.font2().setColor(Color.WHITE);
						
						if(dir.walking){
							dir.walktime += Timers.delta();
						}else{
							dir.walktime = 0;
						}
					}
					
				});
				
				trait.drawShadow(spark, 14, 1);
			}),
			new ColliderTrait(8, 6),
			new ChunkLoadTrait(),
			new SyncTrait(SyncType.player),
			new InputTrait(),
			new HealthTrait(),
			new InventoryTrait(24),
			new DirectionTrait(),
			new EquipTrait(),
			new TileCollideTrait(0, 2, 4, 3)
		);
	}

}
