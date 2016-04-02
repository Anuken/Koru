package net.pixelstatic.koru.components;

import net.pixelstatic.koru.systems.SyncSystem.SyncType;

import com.badlogic.ashley.core.Component;

public class SyncComponent implements Component{
	public final SyncType type;
	
	public SyncComponent(SyncType type){
		this.type = type;
	}
	
	@SuppressWarnings("unused")
	private SyncComponent(){
		type = null;
	};
}
