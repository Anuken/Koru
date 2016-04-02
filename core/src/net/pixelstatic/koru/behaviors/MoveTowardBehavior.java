package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.components.VelocityComponent;




public class MoveTowardBehavior extends Behavior{

	@Override
	protected void update(){
		TargetBehavior target = this.getBehavior(TargetBehavior.class);
		if(target.target != null){
			entity.mapComponent(VelocityComponent.class).velocity.set(target.target.getX() - entity.getX(), target.target.getY() - entity.getY())
			.setLength(0.2f);
		}
	}
}
