package io.anuke.koru.components;

import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.world.materials.BaseMaterial;

@Synced
public class MaterialComponent implements KoruComponent{
	public int matid;
	
	public BaseMaterial material(){
		return BaseMaterial.getMaterial(matid);
	}
}
