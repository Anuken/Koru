package net.pixelstatic.koru.behaviors.groups;

import net.pixelstatic.koru.behaviors.tasks.Task;
import net.pixelstatic.koru.modules.World;

import com.badlogic.gdx.utils.Array;

public abstract class GroupModule{
	protected Array<Task> tasks = new Array<Task>();
	Group group;
	World world;
	
	public abstract void update();
	
	public final void updateInternal(Group group){
		this.group = group;
		if(world == null) world = World.instance();
		update();
	}
}
