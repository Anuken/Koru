package io.anuke.koru.world.materials;

import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.ecs.Spark;

/**A material that is collected on interaction*/
public abstract class CollectableMaterial extends Material{

	protected CollectableMaterial(String name, MaterialType type) {
		super(name, type);
		interactable = true;
	}
	
	public void onInteract(Tile tile, int x, int y, Spark entity){
		entity.get(InventoryTrait.class).addItems(tile.block().getDrops());
		entity.get(InventoryTrait.class).sendUpdate(entity);
			
		tile.setBlockMaterial(Materials.air);
	}
}
