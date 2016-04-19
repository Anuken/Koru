package net.pixelstatic.koru.ai;

import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;

public class AIData{
	IndexedAStarPathFinder<Node> pathfinder;
	ManhattanDistanceHueristic heuristic = new ManhattanDistanceHueristic();
	Graph graph;
	TiledGraphPath<Node> path = new TiledGraphPath<Node>();
	final int node_range = 30;
	Node[][] nodearray = new Node[node_range * 2][node_range * 2];
	Raycaster caster = new Raycaster();
	PathSmoother<Node, Vector2> smoother = new PathSmoother<Node, Vector2>(caster);
	Vector2 lastpos = new Vector2();
	int node = 0;
	//Timer timer = new Timer();
	Node start;
	boolean inobject;
	int ignoredx, ignoredy, axg, ayg;
	World world;
	public boolean foundend;

	public Vector2 CreatePath(float ax, float ay, float bx, float by){
		foundend = false;
		axg = (int)(ax / World.tilesize); //starting x/y
		ayg = (int)((ay) / World.tilesize);
		int bxg = (int)(bx / World.tilesize); //ending x/y
		int byg = (int)(by / World.tilesize);
		int index = 0;
		Node end = null;
		graph = new Graph();
		/*
		boolean broken =false;
		if(solid(axg,ayg)){
			for(int sx = -1; sx <= -1; sx ++){
				for(int sy = -1; sy <= -1; sy ++){
					if((sx == 1 && sy == 1) || (sx == 1 && sy == -1)
							|| (sx == -1 && sy == -1) || (sx == -1 && sy == 1)  || (sx == 0 && sy == 0) ) continue;
					if(!solid(sx + axg, sy + ayg)){
						axg += sx;
						ayg += sy;
						broken = true;
						break;
					}
				}
				if(broken) break;
			}
		}
		*/
		
		for(int x = -node_range;x < node_range;x ++){
			for(int y = -node_range;y < node_range;y ++){
				if(x == 0 && y == 0 ){ // this is the starting point
					nodearray[x + node_range][y + node_range] = new Node(axg + x, ayg + y, index ++);
					graph.addNode(nodearray[x + node_range][y + node_range]);
					start = nodearray[x + node_range][y + node_range];
				}else if(x + axg == bxg && y + ayg == byg){ // this is the end point
					nodearray[x + node_range][y + node_range] = new Node(axg + x, ayg + y, index ++);
					graph.addNode(nodearray[x + node_range][y + node_range]);
					end = nodearray[x + node_range][y + node_range];
					foundend = true;
				}else if(solid(axg + x, ayg + y)){ // this block is solid, ignoring
					nodearray[x + node_range][y + node_range] = null;
				}else{ //otherwise, normal node
					nodearray[x + node_range][y + node_range] = new Node(axg + x, ayg + y, index ++);
					graph.addNode(nodearray[x + node_range][y + node_range]);
				}

			}
		}
		//add linked neighbors
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

		pathfinder = new IndexedAStarPathFinder<Node>(graph, true);
		path.clear();
		pathfinder.searchNodePath(start, end, heuristic, path);
		//smoother.smoothPath(Path);
		//end = path.nodes.peek();
		node = 0;
		lastpos.set(bx, by);
		if(path.nodes.size == 0) return new Vector2(bx, by);
		return new Vector2(end.x * World.tilesize + 6, end.y * World.tilesize + 6);
	}
	
	public boolean solid(int x, int y){
		if(!World.inBounds(x, y)) return true;
		if(world == null) world = KoruUpdater.instance.world;
		return world.tiles[x][y].solid();
	}
	


	private void addNodeNeighbour(Node node, int aX, int aY, int rx, int ry){
		if(aX >= 0 && aX < node_range * 2 && aY >= 0 && aY < node_range * 2){
			node.addNeighbour(nodearray[aX][aY]);
		}
	}
}
