package net.pixelstatic.koru.world;

public enum Material{
	air, 
	grass, 
	stone, 
	pinetree, 
	stoneblock{
		public MaterialType getType(){
			return MaterialType.block;
		}
	};
	
	public MaterialType getType(){
		return MaterialType.tile;
	}
}
