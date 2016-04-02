package net.pixelstatic.koru.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.utils.Array;

public class Node{

	public final static int TILE_SIZE = 12;

	/** Index that needs to be unique for every node and starts from 0. */
	private int mIndex;

	/** X pos of node. */
	public final int x;
	/** Y pos of node. */
	public final int y;

	/** The neighbours of this node. i.e to which node can we travel to from here. */
	Array<Connection<Node>> mConnections = new Array<Connection<Node>>();

	boolean mSelected;

	/** @param aIndex needs to be unique for every node and starts from 0. */
	public Node(int aX, int aY, int aIndex){
		mIndex = aIndex;
		x = aX;
		y = aY;
	}

	public int getIndex(){
		return mIndex;
	}

	public Array<Connection<Node>> getConnections(){
		return mConnections;
	}

	public void select(){
		mSelected = true;
	}

	public void addNeighbour(Node aNode){
		if(null != aNode){
			mConnections.add(new DefaultConnection<Node>(this, aNode));
		}
	}

	public String toString(){
		return String.format("Index:%d x:%d y:%d", mIndex, x, y);
	}

}
