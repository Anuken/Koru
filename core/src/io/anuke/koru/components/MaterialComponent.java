package io.anuke.koru.components;

import io.anuke.koru.network.syncing.SyncData.Synced;
import io.anuke.koru.world.materials.Material;

@Synced
public class MaterialComponent implements KoruComponent{
	public int matid;
	
	public Material material(){
		return Material.getMaterial(matid);
	}
}
