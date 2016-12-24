package io.anuke.koru.ai;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.math.Vector2;

import io.anuke.koru.modules.World;

public class TiledGraphPath extends DefaultGraphPath<Long> implements SmoothableGraphPath<Long, Vector2>{

	private Vector2 tmpPosition = new Vector2();

	/** Returns the position of the node at the given index.
	 * <p>
	 * <b>Note that the same Vector2 instance is returned each time this method is called.</b>
	 * @param index the index of the node you want to know the position of*/
	@Override
	public Vector2 getNodePosition(int index){
		Long node = nodes.get(index);
		
		return tmpPosition.set(World.world(World.getX(node)), World.world(World.getY(node)));
	}

	@Override
	public void swapNodes(int index1, int index2){
		Long node = nodes.get(index1);
		nodes.set(index1, nodes.get(index2));
		nodes.set(index2, node);
	}

	@Override
	public void truncatePath(int newLength){
		nodes.truncate(newLength);
	}

}
