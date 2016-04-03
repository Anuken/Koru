package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.Interpolator;
import net.pixelstatic.koru.systems.SyncSystem.SyncType;

import com.badlogic.ashley.core.Component;

public class SyncComponent implements Component{
	public final SyncType type;
	public final Interpolator interpolator;
	
	public SyncComponent(SyncType type, Interpolator interpolator){
		this.type = type;
		this.interpolator = interpolator;
	}
	
	public SyncComponent(SyncType type){
		this.type = type;
		this.interpolator = null;
	}
	
	@SuppressWarnings("unused")
	private SyncComponent(){
		type = null;
		interpolator = null;
	}
}
