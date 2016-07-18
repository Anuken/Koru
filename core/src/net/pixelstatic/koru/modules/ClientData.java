package net.pixelstatic.koru.modules;

import net.pixelstatic.koru.Koru;
import net.pixelstatic.koru.components.ConnectionComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.utils.modules.Module;

public class ClientData extends Module<Koru>{
	KoruEntity player;
	
	public ClientData(){
		player = new KoruEntity(EntityType.player);
		player.mapComponent(ConnectionComponent.class).name = "your player";
		player.mapComponent(ConnectionComponent.class).local = true;
		//player.position().set(460, 90);
	}

	@Override
	public void update(){

	}

}
