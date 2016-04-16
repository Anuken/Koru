package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.behaviors.TaskBehavior;
import net.pixelstatic.koru.entities.KoruEntity;

public abstract class Task{
	protected KoruEntity entity;
	protected TaskBehavior behavior;
	
	protected abstract void update();
	
	public void internalUpdate(KoruEntity entity, TaskBehavior behavior){
		this.entity = entity;
		this.behavior = behavior;
		update();
	}
	
	public void insertTask(Task task){
		behavior.tasks.insert(0, task);
	}
	
	public void addTask(Task task){
		behavior.tasks.add(task);
	}
	
	protected void finish(){
		behavior.tasks.removeValue(this, true);
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
}
