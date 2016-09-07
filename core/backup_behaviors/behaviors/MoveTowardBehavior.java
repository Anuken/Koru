package net.pixelstatic.koru.behaviors;

import net.pixelstatic.koru.ai.AIData;
import net.pixelstatic.koru.components.VelocityComponent;

import com.badlogic.gdx.math.Vector2;

public class MoveTowardBehavior extends Behavior{
	AIData data;
	
	@Override
	protected void update(){
		if(data == null) data = new AIData();
		TargetBehavior target = this.getBehavior(TargetBehavior.class);
		
		if(target.target != null){
			Vector2 pos = component().data.pathfindTo(entity.getX(), entity.getY(), target.target.getX(), target.target.getY());
			entity.mapComponent(VelocityComponent.class).velocity.set(pos.x - entity.getX(), pos.y - entity.getY())
			.setLength(0.2f);
		}
	}
}
