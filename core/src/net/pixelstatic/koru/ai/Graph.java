package net.pixelstatic.koru.ai;


import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class Graph implements IndexedGraph<Node>{

	@Override
	public Array<Connection<Node>> getConnections(Node fromNode){
		return fromNode.getConnections();
	}

	@Override
	public int getIndex(Node node){
		return node.getIndex();
	}

	@Override
	public int getNodeCount(){
		return 0;
	}

}
