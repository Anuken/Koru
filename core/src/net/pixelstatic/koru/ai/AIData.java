package net.pixelstatic.koru.ai;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class AIData{
	IndexedAStarPathFinder<Node> PathFinder;
	ManhattanDistanceHueristic Heuristic = new ManhattanDistanceHueristic();
	IndexedGraph<Node> graph;
	TiledGraphPath<Node> Path = new TiledGraphPath<Node>();
	final int node_range = 20;
	Node[][] nodearray = new Node[node_range * 2][node_range * 2];
	Raycaster caster = new Raycaster();
	PathSmoother<Node, Vector2> smoother = new PathSmoother<Node, Vector2>(caster);
	Vector2 lastpos = new Vector2();
	int node = 0;
	Timer timer = new Timer();
	Node start;
	boolean inobject;
	int ignoredx, ignoredy, axg, ayg;

	public void CreatePath(float ax, float ay, float bx, float by){
		axg = (int)(ax / Node.TILE_SIZE);
		ayg = (int)(ay / Node.TILE_SIZE);
		//int bxg = (int)(bx / Node.TILE_SIZE);
		//int byg = (int)(by / Node.TILE_SIZE);
		//int index = 0;
		Node end = null;
		graph = new Graph();
		/*
		for(int x = -node_range;x < node_range;x ++){
			for(int y = -node_range;y < node_range;y ++){
				if(x == 0 && y == 0){
					nodearray[x + node_range][y + node_range] = new Node(axg + x, ayg + y, index ++);
					graph.addNode(nodearray[x + node_range][y + node_range]);
					start = nodearray[x + node_range][y + node_range];
				}else if(x + axg == bxg && y + ayg == byg){
					nodearray[x + node_range][y + node_range] = new Node(axg + x, ayg + y, index ++);
					graph.addNode(nodearray[x + node_range][y + node_range]);
					end = nodearray[x + node_range][y + node_range];
				}else if(solid(axg + x, ayg + y)){
					nodearray[x + node_range][y + node_range] = null;
				}else{
					nodearray[x + node_range][y + node_range] = new Node(axg + x, ayg + y, index ++);
					graph.addNode(nodearray[x + node_range][y + node_range]);
				}

			}
		}
		*/

		for(int x = 0;x < node_range * 2;x ++){
			for(int y = 0;y < node_range * 2;y ++){
				if(nodearray[x][y] != null){
					addNodeNeighbour(nodearray[x][y], x + 1, y, x, y);
					addNodeNeighbour(nodearray[x][y], x - 1, y, x, y);
					addNodeNeighbour(nodearray[x][y], x, y + 1, x, y);
					addNodeNeighbour(nodearray[x][y], x, y - 1, x, y);
				}
			}
		}

		PathFinder = new IndexedAStarPathFinder<Node>(graph, true);
		Path.clear();
		PathFinder.searchNodePath(start, end, Heuristic, Path);
		smoother.smoothPath(Path);
		node = 0;
		lastpos.set(bx, by);
		//if(Path.nodes.size == 0) return new Vector2(bx, by);
		//return new Vector2(node.x * 12 + 6, node.y * 12 + 6);
	}
	


	private void addNodeNeighbour(Node aNode, int aX, int aY, int rx, int ry){
		if(aX >= 0 && aX < node_range * 2 && aY >= 0 && aY < node_range * 2){
			aNode.addNeighbour(nodearray[aX][aY]);
		}
	}
}
