package io.anuke.koru.world.materials;

import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.ecs.Spark;

/**A material that is collected on interaction*/
public abstract class CollectableMaterial extends Material{

	protected CollectableMaterial(String name, MaterialLayer type) {
		super(name, type);
		interactable = true;
	}
	
	@Override
	public void onInteract(Tile tile, Spark entity){
		entity.get(InventoryTrait.class).addItems(getDrops());
		entity.get(InventoryTrait.class).sendUpdate(entity);
			
		tile.setWall(Materials.air);
	}
}
