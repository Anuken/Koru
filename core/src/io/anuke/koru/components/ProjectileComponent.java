package io.anuke.koru.components;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.entities.ProjectileType;
import io.anuke.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;

@Synced
public class ProjectileComponent implements Component{
	public ProjectileType type = ProjectileType.bolt;
	private float rotation;
	
	public void setRotation(KoruEntity entity, float rotation){
		entity.mapComponent(VelocityComponent.class).velocity.set(1f,1f).setAngle(rotation).setLength(type.speed());
		this.rotation = rotation;
	}
	
	public float getRotation(){
		return rotation;
	}
}
