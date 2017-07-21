package io.anuke.koru.traits;

import io.anuke.ucore.ecs.Trait;

public class DirectionTrait extends Trait{
	public Direction direction = Direction.front;
	public float walktime;
	
	public void setOrdinal(int i){
		direction = Direction.values()[i];
	}
	
	public static enum Direction{
		right, front, left, back
	}
}
