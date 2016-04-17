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
	
	public void removeCurrentTask(Class<? extends Task> c){
		if(tasks.size == 0) return;
		if(tasks.get(0).getClass() == c) tasks.removeIndex(0);
	}

	public void removeTask(Class<? extends Task> c){
		Object[] to = tasks.begin();
		for(Object object : to){
			Task task = (Task)object;
			if(task == null) continue;
			if(task.getClass() == c) tasks.removeValue(task, true);
		}
		tasks.end();
	}
	
	public boolean hasTask(Class<? extends Task> c){
		for(Task task : tasks){
			if(task.getClass() == c) return true;
		}
		return false;
	}
	
	public boolean isOnTask(Class<? extends Task> c){
		return tasks.size != 0 && tasks.get(0).getClass() == c;
	}
}
