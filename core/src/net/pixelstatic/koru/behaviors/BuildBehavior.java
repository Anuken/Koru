package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.tasks.ChopTreeTask;
import net.pixelstatic.koru.behaviors.tasks.Group;
import net.pixelstatic.koru.behaviors.tasks.PlaceBlockTask;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;

public class BuildBehavior extends TaskBehavior{
	boolean chopping = true;
	boolean clearedtasks = false;

	{
		tasks.add(new ChopTreeTask());
	}

	@Override
	protected void update(){
		super.update();
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);
		int wood = inventory.quantityOf(Item.wood);
		if(Group.instance.taskpool.size == 0 && !hasTask(PlaceBlockTask.class)){
			if(!clearedtasks){
				tasks.clear();
				clearedtasks = true;
			}
		}else{
			if(chopping && wood >= 20){
				addBlocks();
				removeTask(ChopTreeTask.class);
				chopping = false;
			}else if( !chopping && (wood < 3 || tasks.size == 0)){
				tasks.insert(0, new ChopTreeTask());
				chopping = true;
			}
			clearedtasks = false;
		}
	}



	private void addBlocks(){
		if(Group.instance.taskpool.size != 0) tasks.insert(0, Group.instance.taskpool.pop());
	}
}
