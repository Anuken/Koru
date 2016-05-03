package net.pixelstatic.koru.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.utils.Array;

public class Node{
	private int index;
	public final int x;
	public final int y;

	Array<Connection<Node>> connections = new Array<Connection<Node>>();

	boolean mSelected;

	public Node(int x, int y, int index){
		this.index = index;
		this.x = x;
		this.y = y;
	}

	public int getIndex(){
		return index;
	}

	public Array<Connection<Node>> getConnections(){
		return connections;
	}

	public void addNeighbour(Node aNode){
		if(null != aNode){
			connections.add(new DefaultConnection<Node>(this, aNode));
		}
	}

	public String toString(){
		return String.format("Index:%d x:%d y:%d", index, x, y);
	}

}
