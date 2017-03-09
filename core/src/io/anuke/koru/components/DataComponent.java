package io.anuke.koru.components;

import io.anuke.koru.network.syncing.SyncData.Synced;

/**Holds extra data that can't be put into any other component. Bad design? Probably.*/
@Synced
public class DataComponent implements KoruComponent{
	public Object data;
}
