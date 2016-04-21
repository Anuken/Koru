package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.tasks.HarvestResourceTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
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
				tasks.insert(0, task);
			}
		}
	}
	

}
