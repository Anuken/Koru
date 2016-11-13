package io.anuke.koru.components;

import io.anuke.koru.network.SyncBuffer.Synced;

import com.badlogic.ashley.core.Component;

@Synced
public class ChildComponent implements Component{
	public transient float offset;
	public long parent = -1;
}
