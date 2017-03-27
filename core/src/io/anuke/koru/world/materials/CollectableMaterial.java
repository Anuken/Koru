package io.anuke.koru.world.materials;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.world.Tile;

/**A material that is collected on interaction*/
public abstract class CollectableMaterial extends Material{

	protected CollectableMaterial(String name, MaterialType type) {
		super(name, type);
	}
	
	public void onInteract(Tile tile, int x, int y, KoruEntity entity){
		entity.inventory().addItems(tile.block().getDrops());
		entity.inventory().sendUpdate(entity);
			
		tile.setBlockMaterial(Materials.air);
	}
}
