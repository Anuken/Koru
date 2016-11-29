package io.anuke.koru.items;

import io.anuke.koru.network.InputHandler.ClickEvent;

public interface Item{
	
	public default void clickEvent(ClickEvent event){}
	
	public default int getMaxStackSize(){
		return 40;
	}
	
	public String name();
}
