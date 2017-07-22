package io.anuke.koru.entities;

import io.anuke.ucore.ecs.SparkEvent;

public class KoruEvents{
	
	public static interface Unload extends SparkEvent{
		public boolean handle();
	}
}
