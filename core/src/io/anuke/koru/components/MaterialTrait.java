package io.anuke.koru.components;

import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.world.materials.Material;
import io.anuke.ucore.ecs.Trait;

@Synced
public class MaterialTrait extends Trait{
	public int matid;
	
	public Material material(){
		return Material.getMaterial(matid);
	}
}
