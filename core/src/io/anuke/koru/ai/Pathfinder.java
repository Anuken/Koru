package io.anuke.koru.ai;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;

public class Pathfinder{
	static ManhattanDistanceHueristic heuristic = new ManhattanDistanceHueristic();
	static IndexedAStarPathFinder<Long> pathfinder;
	static TiledGraphPath path = new TiledGraphPath();
	
	//TODO make this actually do something
	public static void findPath(float x, float y, float targetx, float targety){
		long start = 0, end = 0;
		pathfinder.searchNodePath(start, end, heuristic, path);
	}
}
