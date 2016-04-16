package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.ai.AIController;
import net.pixelstatic.koru.components.VelocityComponent;

import com.badlogic.gdx.math.Vector2;

public class MoveTowardTask extends Task{
	static final float completerange = 8;
	float targetx, targety;
	
	public MoveTowardTask(float x, float y){
		targetx = x;
		targety = y;
	}
	
	@Override
	protected void update(){
		Vector2 pos = AIController.pathfindTo(behavior.component().data, entity.getX(), entity.getY(), targetx,targety);
		entity.mapComponent(VelocityComponent.class).velocity.set(pos.x - entity.getX(), pos.y - entity.getY())
		.setLength(0.5f);
		
		if(Vector2.dst(entity.getX(), entity.getY(), targetx, targety) < completerange){
			finish();
		}
	}

}
