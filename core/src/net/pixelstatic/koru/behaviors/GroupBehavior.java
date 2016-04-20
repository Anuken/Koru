package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.tasks.HarvestResourceTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.components.ChildComponent;
import net.pixelstatic.koru.components.FadeComponent;
import net.pixelstatic.koru.components.TextComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.items.Item;

public class GroupBehavior extends TaskBehavior{

	{
		tasks.add(new HarvestResourceTask(Item.wood, 20));
	}

	@Override
	protected void update(){
		super.update();
	///	if(KoruUpdater.frameID() % 360 == 0)
	//		entity.log(tasks);
		
		if(!anyTasks()){
			Task task = entity.group().getTask(entity);
			if(task != null){
				indicateTask(task);
				tasks.insert(0, task);
			}
			
		}
	}
	
	void indicateTask(Task task){
		KoruEntity entity = new KoruEntity(EntityType.damageindicator);
		entity.mapComponent(TextComponent.class).text = task.getClass().getSimpleName();
		entity.mapComponent(ChildComponent.class).parent = this.entity.getID();
		entity.mapComponent(FadeComponent.class).lifetime = 60;
		entity.sendSelf();
	}
}
