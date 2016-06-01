package net.pixelstatic.koru.systems;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.components.GroupComponent;
import net.pixelstatic.koru.components.PositionComponent;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GroupSystem extends KoruSystem{

	public GroupSystem(){
		super(Family.all(GroupComponent.class).exclude(PositionComponent.class).get());
	}

	@Override
	void processEntity(KoruEntity entity, float delta){
		Group group = entity.mapComponent(GroupComponent.class).group;
		if(group == null) throw new GdxRuntimeException("Group entity does not have a group object assigned!");
		group.update();
	}

}
