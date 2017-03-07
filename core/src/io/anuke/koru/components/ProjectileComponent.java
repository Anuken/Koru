package io.anuke.koru.components;

import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.ProjectileType;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.SyncData.Synced;

@Synced
public class ProjectileComponent implements KoruComponent{
	public ProjectileType type = ProjectileType.bolt;
	public transient ObjectSet<Long> hit = IServer.active() ? new ObjectSet<Long>() : null;
	private float rotation;
	
	public void setRotation(KoruEntity entity, float rotation){
		//entity.mapComponent(VelocityComponent.class).velocity.set(1f,1f).setAngle(rotation).setLength(type.speed());
		this.rotation = rotation;
	}
	
	public float getRotation(){
		return rotation;
	}
}
