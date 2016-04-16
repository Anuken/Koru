package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.components.InventoryComponent;
import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.world.Material;

import com.badlogic.gdx.math.MathUtils;

public class BuildBehavior extends Behavior{

	@Override
	protected void update(){
		if(entity.mapComponent(InventoryComponent.class).quantityOf(Item.wood) < 3){
			component().insertBehavior(0, new ChopTreeBehavior());
			removeSelf();
		}else{
			component().insertBehavior(0, new PlaceBlockBehavior(Material.woodblock, 
					entity.position().blockX()+ 3 + MathUtils.random(3), entity.position().blockY() + 3 + MathUtils.random(3)));
			entity.mapComponent(InventoryComponent.class).removeItem(new ItemStack(Item.wood, 3));
		}
	}
}
