package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.ai.AIController;
import net.pixelstatic.koru.components.VelocityComponent;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.utils.DirectionUtils;

import com.badlogic.gdx.math.Vector2;

public class MoveTowardTask extends Task{
	static final float speed = 2f;
	static final float completerange = 11;
	private final int x, y;
	private float selfcompleterange = -1;
	private static Vector2 vector = new Vector2();

	public MoveTowardTask(int x, int y){
		this.x = x;
		this.y = y;
	}

	public MoveTowardTask(int x, int y, float completerange){
		this(x, y);
		this.selfcompleterange = completerange;
	}

	@Override
	protected void update(){
		float targetx = 0, targety = 0;
		targetx = World.world(x);
		targety = World.world(y);
		if(World.instance().blockSolid(x, y)){
			for(int i = 0;i < 4;i ++){
				int sx = DirectionUtils.toX(i), sy = DirectionUtils.toY(i);
				if( !World.instance().blockSolid(x + sx, y + sy)){
					targetx = World.world(x) + sx / 2f;
					targety = World.world(y) + sy / 2f;
					break;
				}
			}
		}

		if(World.instance().tiles[x][y].block.solid()) selfcompleterange = -1;

		//Koru.log(targetx + " " + targety);
		Vector2 pos = AIController.pathfindTo(behavior.component().data, entity.getX(), entity.getY(), targetx, targety);
		entity.mapComponent(VelocityComponent.class).velocity.add(vector.set(pos.x - entity.getX(), pos.y - entity.getY()).setLength(speed));

		if(Vector2.dst(entity.getX(), entity.getY(), targetx, targety) < (selfcompleterange > 0 ? selfcompleterange : completerange)){
			finish();
		}
	}

}
