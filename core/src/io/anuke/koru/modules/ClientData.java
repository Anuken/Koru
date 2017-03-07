package io.anuke.koru.modules;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.types.Player;
import io.anuke.ucore.modules.Module;

//TODO make this actually do something instead of storing a single object
public class ClientData extends Module<Koru>{
	public final KoruEntity player;
	
	public ClientData(){
		player = new KoruEntity(Player.class);
		player.connection().name = "you";
		player.connection().local = true;
	}

	@Override
	public void update(){
		
	}
}
