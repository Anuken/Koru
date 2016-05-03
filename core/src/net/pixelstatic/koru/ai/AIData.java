package net.pixelstatic.koru.ai;


import net.pixelstatic.koru.components.FadeComponent;
import net.pixelstatic.koru.components.TextComponent;
import net.pixelstatic.koru.entities.EntityType;
import net.pixelstatic.koru.entities.KoruEntity;
import net.pixelstatic.koru.modules.World;
import net.pixelstatic.koru.server.KoruUpdater;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;

public class AIData{
	static Node[][] nodes = new Node[World.worldwidth][World.worldheight];
	static Graph graph = new Graph();
	static ManhattanDistanceHueristic heuristic = new ManhattanDistanceHueristic();
	static IndexedAStarPathFinder<Node> pathfinder;
	static TiledGraphPath<Node> path = new TiledGraphPath<Node>();
	static Raycaster caster = new Raycaster();
	static PathSmoother<Node, Vector2> smoother = new PathSmoother<Node, Vector2>(caster);
	static Vector2 v = new Vector2();
	static int space = 14;
	static int skip = 1;
	static public final float changerange = 6;
	static boolean straight = false;
	Vector2 lastpos = new Vector2();
	World world;
	int node;

	static{
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

	public Vector2 pathfindTo(float x, float y, float targetx, float targety){

		if(straight /*|| !cast(x, y, targetx, targety)*/) return v.set(targetx, targety);

		if(lastpos.dst(v.set(targetx, targety)) > changerange || AIData.path == null || World.instance().updated() || KoruUpdater.frameID() % 180 == 0){
			createPath(x, y, targetx, targety);
		}

		if(AIData.path.nodes.size <= 1){
			return AIData.path.nodes.size == 0 ? v.set(targetx, targety) : v.set(AIData.path.getNodePosition(0));
		}

		if(node + 2 >= path.nodes.size){
			return v.set(targetx, targety);
		}else if(AIData.path.getNodePosition(1 + node).dst(v.set(x, y)) <= 1f){
			node ++;
		}

		v.set(AIData.path.getNodePosition(1 + node));
		return v;
	}

	private void createPath(float startx, float starty, float endx, float endy){
		node = 0;
		Node end = null, start = null;

		start = nodes[World.tile(startx)][World.tile(starty)];
		end = nodes[World.tile(endx)][World.tile(endy)];

		path.clear();
		pathfinder.searchNodePath(start, end, heuristic, path);
		lastpos.set(endx, endy);
		int i = 0;
		for(Node node : path.nodes){
			KoruEntity entity = new KoruEntity(EntityType.damageindicator);
			entity.mapComponent(TextComponent.class).text = "n" + i++;
			entity.mapComponent(FadeComponent.class).lifetime = 180;
			entity.position().set(World.world(node.x), World.world(node.y));
			entity.sendSelf();
		}
		
	}

	public boolean solid(int x, int y){
		if( !World.inBounds(x, y)) return true;
		if(world == null) world = KoruUpdater.instance.world;
		return world.tiles[x][y].solid();
	}

	static void addNodeNeighbour(Node node, int x, int y){
		if(x >= 0 && x < World.worldwidth && y >= 0 && y < World.worldheight){
			node.addNeighbour(nodes[x][y]);
		}
	}
}
