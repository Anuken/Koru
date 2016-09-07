package net.pixelstatic.koru.ai;


public class AIData{
	/*
	static final boolean debug = false;
	static Node[][] nodes = new Node[1][1];
	static Graph graph = new Graph();
	static ManhattanDistanceHueristic heuristic = new ManhattanDistanceHueristic();
	static IndexedAStarPathFinder<Node> pathfinder;
	static Raycaster caster = new Raycaster();
	static PathSmoother<Node, Vector2> smoother = new PathSmoother<Node, Vector2>(caster);
	static Vector2 v = new Vector2();
	static int space = 14;
	static int skip = 1;
	static public final float changerange = 6;
	static boolean straight = false;
	static private World world;
	TiledGraphPath<Node> path = new TiledGraphPath<Node>();
	Vector2 lastpos = new Vector2();
	int node;

	static{
		/*
		if(KoruServer.active){
			for(int x = 0;x < World.worldwidth;x ++){
				for(int y = 0;y < World.worldheight;y ++){
					nodes[x][y] = new Node(x, y, graph.nodes.size);
					graph.addNode(nodes[x][y]);
				}
			}
			for(int x = 0;x < World.worldwidth;x ++){
				for(int y = 0;y < World.worldheight;y ++){
					addNodeNeighbour(nodes[x][y], x + 1, y);
					addNodeNeighbour(nodes[x][y], x - 1, y);
					addNodeNeighbour(nodes[x][y], x, y + 1);
					addNodeNeighbour(nodes[x][y], x, y - 1);
				}
			}
			pathfinder = new IndexedAStarPathFinder<Node>(graph, true);
		}
		//end
	}

	public static void updateNode(int x, int y){
		updateConnections(x, y);
		updateConnections(x - 1, y);
		updateConnections(x + 1, y);
		updateConnections(x, y - 1);
		updateConnections(x, y + 1);

	}

	private static void updateConnections(int x, int y){
		//if(!World.inBounds(x, y)) return;
		Node node = nodes[x][y];
		node.connections.clear();
		addNodeNeighbour(node, node.x + 1, node.y);
		addNodeNeighbour(node, node.x - 1, node.y);
		addNodeNeighbour(node, node.x, node.y + 1);
		addNodeNeighbour(node, node.x, node.y - 1);
	}

	public Vector2 pathfindTo(float x, float y, float targetx, float targety){

		if(straight ) return v.set(targetx, targety);

		if(lastpos.dst(v.set(targetx, targety)) > changerange || path == null || World.instance().updated() || KoruUpdater.frameID() % 180 == 0){
			if(debug) Effects.indicator("recalc", Color.GREEN, x, y + 10, 100);
			createPath(x, y, targetx, targety);
		}

		if(path.nodes.size <= 1){
			return path.nodes.size == 0 ? v.set(targetx, targety) : v.set(path.getNodePosition(0));
		}

		if(node + 2 >= path.nodes.size){
			return v.set(targetx, targety);
		}else if(path.getNodePosition(1 + node).dst(v.set(x, y)) <= 2f){
			node ++;
		}
		//Koru.log(path.get(1+node).solid());
		v.set(path.getNodePosition(1 + node));
		return v;
	}

	private void createPath(float startx, float starty, float endx, float endy){
		node = 0;
		Node end = null, start = null;

		start = nodes[World.tile(startx)][World.tile(starty)];
		end = nodes[World.tile(endx)][World.tile(endy)];

		path.clear();
		heuristic.set(start, end);
		pathfinder.searchNodePath(start, end, heuristic, path);
		lastpos.set(endx, endy);
		int i = 0;
		if(debug) for(Node node : path.nodes){
			Effects.indicator("n" + i ++, Color.RED, World.world(node.x), World.world(node.y), 180);
		}

	}

	public static boolean solid(int x, int y){
		if(world == null) world = KoruUpdater.instance.world;
		return world.tile(x, y).solid();
	}

	static void addNodeNeighbour(Node node, int x, int y){
		node.addNeighbour(nodes[x][y]);

	}
	*/
}
