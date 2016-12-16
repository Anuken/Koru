package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.network.Interpolator;
import io.anuke.koru.systems.SyncSystem.SyncType;

public class SyncComponent implements Component{
	public final SyncType type;
	public final Interpolator interpolator;
	
	public SyncComponent(SyncType type, Interpolator interpolator){
		this.type = type;
		this.interpolator = interpolator;
	}
	
	public SyncComponent(SyncType type){
		this.type = type;
		this.interpolator = new Interpolator();
	}
	
	@SuppressWarnings("unused")
	private SyncComponent(){
		type = null;
		interpolator = null;
	}
}
