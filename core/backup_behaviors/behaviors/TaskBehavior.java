package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.server.KoruUpdater;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;


public class TaskBehavior extends Behavior{
	private static final float moverange = 0.07f;
	private static final float stucktime = 120;
	public SnapshotArray<Task> tasks = new SnapshotArray<Task>();
	public Task lastfailed;
	private Vector2 lastpos = new Vector2();
	private float movetime;
	
	
	private void updatePosition(){
		if(lastpos.dst(entity.getX(), entity.getY()) > moverange){
			movetime = 0;
		}else{
			movetime += KoruUpdater.instance.delta();
		}
		lastpos.set(entity.getX(), entity.getY());
	}
	
	public void resetTimer(){
		movetime = 0;
	}
	
	@Override
	protected void update(){
		updatePosition();
		Object[] objects = tasks.begin();
		if(objects.length != 0 && objects[0] != null)((Task)objects[0]).internalUpdate(entity, this);
		tasks.end();
	}
	
	public void removeCurrentTask(Class<? extends Task> c){
		if(tasks.size == 0) return;
		if(tasks.get(0).getClass() == c) tasks.removeIndex(0);
	}
	
	public boolean anyTasks(){
		return tasks.size != 0;
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
	
	public boolean stuck(){
		return movetime > stucktime;
	}
}
