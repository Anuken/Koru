package net.pixelstatic.koru.items;

public enum Item{
	stick, wood, pinecone, water;
	
	private Item(){
		
	}
	
	private Item(int stacksize){
		this.stacksize = stacksize;
	}
	
	private int stacksize = 999;
	
	public int getMaxStackSize(){
		return stacksize;
	}
}
