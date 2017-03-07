package io.anuke.koru.modules;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.ucore.modules.Module;

//TODO make this actually do something instead of storing a single object
public class ClientData extends Module<Koru>{
	public final KoruEntity player;
	
	public ClientData(){
		player = new KoruEntity(EntityType.player);
		player.connection().name = "your player";
		player.connection().local = true;
	}

	@Override
	public void update(){
		
	}
}
