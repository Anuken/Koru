package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.ai.AIController;
import net.pixelstatic.koru.components.VelocityComponent;
import net.pixelstatic.koru.modules.World;

import com.badlogic.gdx.math.Vector2;

public class MoveTowardTask extends Task{
	static final float speed = 2f;
	static final float completerange = 11;
	private int x, y;
	
	public MoveTowardTask(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	protected void update(){
		float targetx=0, targety=0;
		if(World.instance().blockSolid(x, y)){
			
		}else{
			targetx = x*World.tilesize+6;
			targety = y*World.tilesize+6;
		}
		Vector2 pos = AIController.pathfindTo(behavior.component().data, entity.getX(), entity.getY(), targetx,targety);
		entity.mapComponent(VelocityComponent.class).velocity.set(pos.x - entity.getX(), pos.y - entity.getY())
		.setLength(speed);
		
		if(Vector2.dst(entity.getX(), entity.getY(), targetx, targety) < completerange){
			finish();
		}
	}

}
