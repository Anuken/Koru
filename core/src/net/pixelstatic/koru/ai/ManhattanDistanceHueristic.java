package net.pixelstatic.koru.ai;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanDistanceHueristic implements Heuristic<Node>{
    @Override
    public float estimate(Node node, Node endNode){
	if(node == null || endNode == null) return 0;
	return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
    }
}
