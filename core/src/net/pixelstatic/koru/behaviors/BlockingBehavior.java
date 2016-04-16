package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.server.KoruUpdater;

public class BlockingBehavior extends Behavior{
	final float duration;
	float time;

	public BlockingBehavior(float duration){
		this.duration = duration;
		this.blocking = true;
	}

	@Override
	protected void update(){
		time += KoruUpdater.instance.delta();
		if(time > duration) removeSelf();
	}
}
