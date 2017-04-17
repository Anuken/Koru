package io.anuke.koru.items;

public class Tools{
	//<--increasing length-->
	private static ToolType[][] matrix = {
		{new ToolType("Dagger"), new ToolType("Axe"), new ToolType("Spear")},
		{new ToolType("Claws"), new ToolType("Pickaxe"), new ToolType("Hammer")},
		{new ToolType("Shield"), new ToolType("Sword"), new ToolType("Greatsword")}
	};
	
	public static String getToolName(int length, int size){
		return matrix[size][length].name;
	}
	
	static class ToolType{
		private final String name;
		
		public ToolType(String name){
			this.name = name;
		}
	}
}
