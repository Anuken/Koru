package io.anuke.koru.components;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.SyncType;
import io.anuke.koru.network.syncing.Interpolator;

public class SyncComponent implements KoruComponent{
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
	
	private SyncComponent(){
		type = null;
		interpolator = null;
	}
	
	@Override
	public void update(KoruEntity entity){
		if(IServer.active() || (entity.connection() != null && entity.connection().local == true)) return;
		SyncComponent sync = entity.get(SyncComponent.class);

		if(sync.interpolator != null) sync.interpolator.update(entity);
	}
}
