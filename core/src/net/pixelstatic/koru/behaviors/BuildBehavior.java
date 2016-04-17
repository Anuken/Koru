package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.behaviors.tasks.ChopTreeTask;
import net.pixelstatic.koru.behaviors.tasks.PlaceBlockTask;
import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;

public class BuildBehavior extends TaskBehavior{

	{
		tasks.add(new ChopTreeTask());
	}

	@Override
	protected void update(){
		super.update();
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);
		int wood = inventory.quantityOf(Item.wood);
		/*
		if(Group.instance.tasks() == 0 && !hasTask(PlaceBlockTask.class)){
			if(!clearedtasks){
				tasks.clear();
				clearedtasks = true;
			}
		}else{
		*/
		if(wood >= 20 && !this.hasTask(PlaceBlockTask.class)){
			addBlocks();
			removeTask(ChopTreeTask.class);
		}else if(wood < 3 && !this.hasTask(ChopTreeTask.class)){
			tasks.insert(0, new ChopTreeTask());
		}
		//	}
	}

	private void addBlocks(){
		Task task = Group.instance.getTask();
		if(task != null) tasks.insert(0, task);
	}
}
