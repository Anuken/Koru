package io.anuke.koru.items;

import io.anuke.koru.network.InputHandler.ClickEvent;
import io.anuke.koru.world.Material;

public interface Item{
	
	public default void clickEvent(ClickEvent event){}
	
	public default int getMaxStackSize(){
		return 40;
	}
	
	public default boolean tool(){return false;}
	public default boolean breaks(Material mat){return false;}
	public default float power(){return 0;}
	
	public String name();
}
