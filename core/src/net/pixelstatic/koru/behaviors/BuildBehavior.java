package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.groups.Group;
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
		/*
		if(Group.instance.tasks() == 0 && !hasTask(PlaceBlockTask.class)){
			if(!clearedtasks){
				tasks.clear();
				clearedtasks = true;
			}
		}else{
		*/
		
		if(!anyTasks()){
			addBlockTask();
		}
		/*
		if(hasTask(HarvestResourceTask.class)){  // is chopping wood
			if(wood >= 20){ // has 20 wood, so stop chopping and start placing blocks
				removeTask(HarvestResourceTask.class);
				addBlockTask();
			}
		}else if(!anyTasks()){ // no tasks
			if(wood >= 3){ // has wood to place another block
				addBlockTask();
			}else{ //else, chop more trees to get wood
				tasks.add(new HarvestResourceTask(Item.wood));
			}
		}
		*/
		/*
		if(wood >= 20 && !this.hasTask(PlaceBlockTask.class)){
			addBlockTask();
			removeTask(ChopTreeTask.class);
		}else if(wood < 3 && !this.hasTask(ChopTreeTask.class)){
			tasks.insert(0, new ChopTreeTask());
		}
		*/
		//	}
	}

	private void addBlockTask(){
		Task task = Group.instance.getTask();
		if(task != null) tasks.insert(0, task);
	}
}
