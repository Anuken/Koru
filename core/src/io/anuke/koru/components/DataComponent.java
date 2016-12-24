package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.network.SyncData.Synced;

/**Holds extra data that can't be put into any other component. Bad design? Probably.*/
@Synced
public class DataComponent implements Component{
	public Object data;
}
