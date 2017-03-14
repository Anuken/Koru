package io.anuke.koru.ai;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanDistanceHueristic implements Heuristic<Long>{

	@Override
	public float estimate(Long node, Long other){
		//float x = World.world(World.getX(node)), y = World.world(World.getY(node));
		//float x2 = World.world(World.getX(other)), y2 = World.world(World.getY(other));
		//return Math.abs(x2 - x) + Math.abs(y2 - y);
		return 0;
	}
}
