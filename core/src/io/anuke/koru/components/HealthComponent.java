package io.anuke.koru.components;

import io.anuke.koru.network.syncing.SyncData.Synced;

@Synced
public class HealthComponent implements KoruComponent{
	public transient int maxhealth = 100;
	public int health = maxhealth;
	
	public HealthComponent(int health){
		this.maxhealth = this.health = health;
	}
	
	public HealthComponent(){}
}
