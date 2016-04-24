package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.behaviors.TaskBehavior;
import net.pixelstatic.koru.components.ChildComponent;
import net.pixelstatic.koru.components.FadeComponent;
import net.pixelstatic.koru.components.TextComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;

public abstract class Task{
	protected KoruEntity entity;
	public TaskBehavior behavior;
	protected Task parent;
	private FailReason reason;
	
	protected abstract void update();
	
	public void internalUpdate(KoruEntity entity, TaskBehavior behavior){
		this.entity = entity;
		this.behavior = behavior;
		update();
	}
	
	public void insertTask(Task task){
		behavior.resetTimer();
		behavior.tasks.insert(0, task);
		task.parent = this;
		//indicateTask();
	}
	
	public void addTask(Task task){
		behavior.resetTimer();
		behavior.tasks.add(task);
		task.parent = this;
	}
	
	protected void finish(){
		behavior.tasks.removeValue(this, true);
	}
	
	protected void finish(FailReason reason){
		behavior.tasks.removeValue(this, true);
		if(reason == null) return;
		this.reason = reason;
		if(parent != null) parent.notifyFailed(this, reason);
		entity.groupc().group.notifyTaskFailed(entity, this, this.reason);
	}
	
	public boolean failed(){
		return reason != null;
	}
	
	public FailReason getFailReason(){
		return reason;
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	public void notifyFailed(Task task, FailReason reason){
		
	}
	
	public boolean stuck(){
		return behavior.stuck();
	}
	
	void indicateTask(){
		KoruEntity entity = new KoruEntity(EntityType.damageindicator);
		entity.mapComponent(TextComponent.class).text = this.getClass().getSimpleName();
		entity.mapComponent(ChildComponent.class).parent = this.entity.getID();
		entity.mapComponent(FadeComponent.class).lifetime = 60;
		entity.sendSelf();
	}
	
	public static enum FailReason{
		stuck
	}
}
