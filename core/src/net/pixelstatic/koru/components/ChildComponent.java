package net.pixelstatic.koru.components;

import net.pixelstatic.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;

@Synced
public class ChildComponent implements Component{
	public transient float offset;
	public long parent = -1;
}
