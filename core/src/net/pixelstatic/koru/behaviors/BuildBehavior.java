package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.tasks.HarvestResourceTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.items.Item;

public class BuildBehavior extends TaskBehavior{

	{
		tasks.add(new HarvestResourceTask(Item.wood, 20));
	}

	@Override
	protected void update(){
		super.update();
		
		if(!anyTasks()){
			addBlockTask();
		}
	}

	private void addBlockTask(){
		Task task = entity.group().getTask();
		if(task != null) tasks.insert(0, task);
	}
}
