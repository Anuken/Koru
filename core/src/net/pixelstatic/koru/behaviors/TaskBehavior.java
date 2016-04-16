package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.tasks.Task;

import com.badlogic.gdx.utils.SnapshotArray;


public class TaskBehavior extends Behavior{
	public SnapshotArray<Task> tasks = new SnapshotArray<Task>();
	
	@Override
	protected void update(){
		Object[] objects = tasks.begin();
		if(objects.length != 0 && objects[0] != null)((Task)objects[0]).internalUpdate(entity, this);
		tasks.end();
	}
}
