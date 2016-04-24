package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.server.KoruUpdater;

public class WaitTask extends Task{
	final float duration;
	float time;
	
	public WaitTask(float duration){
		this.duration = duration;
	}
	
	@Override
	protected void update(){
		time += KoruUpdater.instance.delta();
		if(time > duration){
			finish();
		}
	}

}
