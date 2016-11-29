package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.network.SyncBuffer.Synced;

@Synced
public class DataComponent implements Component{
	public Object data;
}
