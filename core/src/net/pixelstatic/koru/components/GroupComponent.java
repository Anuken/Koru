package net.pixelstatic.koru.components;

import net.pixelstatic.koru.behaviors.groups.Group;
import net.pixelstatic.koru.behaviors.groups.Structure;
import net.pixelstatic.koru.entities.KoruEntity;

import com.badlogic.ashley.core.Component;

public class GroupComponent implements Component{
	public Group group;
	public Structure structure;
	
	public GroupComponent(Group group){
		this.group = group;
	}
	
	public void init(KoruEntity entity){
		group.addEntity(entity);
	}
}
