package io.anuke.koru.ai;


import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongArray;

//TODO switch to an actual Node class with an index instead of Longs
//TODO pooling
public class Graph implements IndexedGraph<Long>{
	LongArray nodes = new LongArray();
	private Array<Connection<Long>> tempConnections = new Array<Connection<Long>>();
	
	@Override
	public Array<Connection<Long>> getConnections(Long fromNode){
		//TODO return the connections
		return tempConnections;
	}

	@Override
	public int getIndex(Long node){
		//TODO return the actual index
		return 0;
	}

	@Override
	public int getNodeCount(){
		return nodes.size;
	}
	
	public void addNode(Long node){
		nodes.add(node);
	}
}
