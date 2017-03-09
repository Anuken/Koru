package io.anuke.koru.components;

import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.koru.entities.ProjectileType;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.syncing.SyncData.Synced;

@Synced
public class ProjectileComponent implements KoruComponent{
	public ProjectileType type = ProjectileType.bolt;
	public transient ObjectSet<Long> hit = IServer.active() ? new ObjectSet<Long>() : null;
}
