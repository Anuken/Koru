package net.pixelstatic.koru.behaviors;

import com.badlogic.gdx.utils.Array;

public class TaskBehavior extends Behavior{
	Array<Behavior> tasks = new Array<Behavior>();
	
	@Override
	protected void update(){
		if(tasks.size != 0)
			tasks.first().update();
	}
}
