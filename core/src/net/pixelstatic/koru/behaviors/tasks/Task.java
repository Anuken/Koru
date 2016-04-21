package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.behaviors.TaskBehavior;
import net.pixelstatic.koru.components.ChildComponent;
import net.pixelstatic.koru.components.FadeComponent;
import net.pixelstatic.koru.components.TextComponent;
import net.pixelstatic.koru.entities.EntityType;
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
	//	indicateTask();
	}
	
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	void indicateTask(){
		KoruEntity entity = new KoruEntity(EntityType.damageindicator);
		entity.mapComponent(TextComponent.class).text = this.getClass().getSimpleName();
		entity.mapComponent(ChildComponent.class).parent = this.entity.getID();
		entity.mapComponent(FadeComponent.class).lifetime = 60;
		entity.sendSelf();
	}
}
