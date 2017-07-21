package io.anuke.koru.components;

import io.anuke.koru.network.IServer;
import io.anuke.koru.network.SyncType;
import io.anuke.koru.network.syncing.Interpolator;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;

public class SyncTrait extends Trait{
	public final SyncType type;
	public final Interpolator interpolator;
	
	public SyncTrait(SyncType type, Interpolator interpolator){
		this.type = type;
		this.interpolator = interpolator;
	}
	
	public SyncTrait(SyncType type){
		this.type = type;
		this.interpolator = new Interpolator();
	}
	
	private SyncTrait(){
		type = null;
		interpolator = null;
	}
	
	//TODO
	@Override
	public void update(Spark spark){
		if(IServer.active() || (spark.has(ConnectionTrait.class) && spark.get(ConnectionTrait.class).local)) return;
		SyncTrait sync = spark.get(SyncTrait.class);

		if(sync.interpolator != null) sync.interpolator.update(spark);
	}
}
