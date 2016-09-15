package io.anuke.koru.ai;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanDistanceHueristic implements Heuristic<Node>{
	public Node start, end;

	public void set(Node s, Node e){
		start = s;
		end = e;
	}

	@Override
	public float estimate(Node node, Node other){
		//if(node == start || node == end || other == start || other == end) return 0;
		if(node.solid() || other.solid()){
		//	Koru.log("Solid node " + node + " detected.");
		//	return 99999999;
		}
		return Math.abs(other.x - node.x) + Math.abs(other.y - node.y);
	}
}
