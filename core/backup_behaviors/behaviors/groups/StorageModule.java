package net.pixelstatic.koru.behaviors.groups;

import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.entities.KoruEntity;

public class StorageModule extends GroupModule{
	int storage;
	
	public Task getTask(KoruEntity entity){
		if(entity.mapComponent(InventoryComponent.class).usedSlots() > 3){
			//if(storage < 1){
				
			//}else{
				
			//}
		//	return new StoreItemTask(1,1);
		}
		return null;
	}
	
	@Override
	public void update(){
		//for(KoruEntity entity : group.entities){
			/*
			if(entity.mapComponent(InventoryComponent.class).usedSlots() > 3 && 
					!entity.mapComponent(BehaviorComponent.class).getBehavior(GroupBehavior.class).hasTask(StoreItemTask.class)){
				//entity.mapComponent(BehaviorComponent.)
			}
			*/
		//}
	}
}
