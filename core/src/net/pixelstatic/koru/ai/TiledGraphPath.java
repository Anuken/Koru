package net.pixelstatic.koru.ai;

import net.pixelstatic.koru.world.World;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.math.Vector2;

public class TiledGraphPath<N extends Node> extends DefaultGraphPath<N> implements SmoothableGraphPath<N, Vector2>{

	private Vector2 tmpPosition = new Vector2();

	/** Returns the position of the node at the given index.
	 * <p>
	 * <b>Note that the same Vector2 instance is returned each time this method is called.</b>
	 * @param index the index of the node you want to know the position of*/
	@Override
	public Vector2 getNodePosition(int index){
		N node = nodes.get(index);
		return tmpPosition.set(World.world(node.x), World.world(node.y));
	}

	@Override
	public void swapNodes(int index1, int index2){
		N node = nodes.get(index1);
		nodes.set(index1, nodes.get(index2));
		nodes.set(index2, node);
	}

	@Override
	public void truncatePath(int newLength){
		nodes.truncate(newLength);
	}

}
