package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.behaviors.tasks.ChopTreeTask;
import net.pixelstatic.koru.behaviors.tasks.Group;
import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;


public class BuildBehavior extends TaskBehavior{
	boolean chopping = true;

	{
		tasks.add(new ChopTreeTask());
	}
	
	@Override
	protected void update(){
		super.update();
		InventoryComponent inventory = entity.mapComponent(InventoryComponent.class);
		int wood = inventory.quantityOf(Item.wood);
	//	if(KoruUpdater.frameID() % 40 == 0)System.out.println("Current state: " + tasks);
		if(chopping && wood >= 20){
			addBlocks();
			removeTask(ChopTreeTask.class);
			chopping = false;
		}else if(!chopping && (wood < 3 || tasks.size == 0)){
			tasks.insert(0,new ChopTreeTask());
			chopping = true;
		}
		/*
		if(entity.mapComponent(InventoryComponent.class).quantityOf(Item.wood) < 3){
			component().insertBehavior(0, new ChopTreeBehavior());
			removeSelf();
		}else{
			component().insertBehavior(0, new PlaceBlockBehavior(Material.woodblock, 
					entity.position().blockX()+ 3 + MathUtils.random(3), entity.position().blockY() + 3 + MathUtils.random(3)));
			entity.mapComponent(InventoryComponent.class).removeItem(new ItemStack(Item.wood, 3));
		}
		*/
	}
	
	public void removeTask(Class<? extends Task> c){
		Object[] to = tasks.begin();
		for(Object object : to){
			Task task = (Task)object;
			if(task == null) continue;
			if(task.getClass() == c)
				tasks.removeValue(task, true);
		}
		tasks.end();
	}
	
	private void addBlocks(){
		if(Group.instance.taskpool.size != 0)
		tasks.insert(0, Group.instance.taskpool.pop());
		/*
		int ex = entity.position().blockX();
		int ey = entity.position().blockY();
		for(int x = 0; x < blocks.length; x ++){
			for(int y = 0; y < blocks[x].length; y ++){
				int worldx = ex + x, worldy = ey+y;
				if(blocks[x][y] != 0){
					tasks.add(new MoveTowardTask(worldx*12+6, (worldy+2)*12+6));
					tasks.add(new PlaceBlockTask(worldx, worldy, Material.woodblock));
				}
			}
		}
		*/
	}
}
