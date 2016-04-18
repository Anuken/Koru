package net.pixelstatic.koru.world;

import net.pixelstatic.koru.items.Item;
import net.pixelstatic.koru.items.ItemStack;
import net.pixelstatic.koru.utils.Colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public enum Material{
	air, 
	grass(Colors.colora(69, 109, 29,0.04f)), 
	water(MaterialType.water, true){
		{addDrop(Item.water, 1);}
		
		public boolean reserved(){
			return false;
		}
	},
	riveredge,
	stone, 
	woodfloor, 
	pinecones(MaterialType.overlay){{addDrop(Item.pinecone, 1);}}, 
	tallgrass1(MaterialType.grass),
	tallgrass2(MaterialType.grass),
	tallgrass3(MaterialType.grass),
	fern1(MaterialType.grass),
	fern2(MaterialType.grass),
	fern3(MaterialType.grass),
	koru1(MaterialType.grass),
	koru2(MaterialType.grass),
	koru3(MaterialType.grass),
	pinetree1(MaterialType.tree, true){{addDrop(Item.wood, 5); addDrop(Item.pinecone, 1);}}, 
	pinetree2(MaterialType.tree, true){{addDrop(Item.wood, 5); addDrop(Item.pinecone, 2);}}, 
	pinetree3(MaterialType.tree, true){{addDrop(Item.wood, 5); addDrop(Item.pinecone, 2);}}, 
	pinetree4(MaterialType.tree, true){{addDrop(Item.wood, 5);}}, 
	pinesapling(MaterialType.tree, false, false){
		{addDrop(Item.pinecone, 1);}
		
		public boolean growable(){
			return true;
		}
		
		public Material growMaterial(){
			return random(Material.pinetree1, Material.pinetree2, Material.pinetree3, Material.pinetree4);
		}
		
		public ItemStack getGrowItem(){
			return new ItemStack(Item.water, 4);
		}
	}, 
	stoneblock(MaterialType.block),
	woodblock(MaterialType.block){{addDrop(Item.wood, 3);}},
	box(MaterialType.chest){
		public TileData getDefaultData(){
			return new InventoryTileData(5,5);
		}
		
		public Class<? extends TileData> getDataClass(){
			return InventoryTileData.class;
		}
	};
	
	private MaterialType type = MaterialType.tile;
	private Color foilageColor = Colors.colora(69, 109, 29,0.04f);
	private boolean breakable;
	private boolean enablecollisions = true;
	private Array<ItemStack> drops = new Array<ItemStack>();
	
	private Material(){
		
	}
	
	private Material(MaterialType type){
		this.type = type;
	}
	
	private Material(MaterialType type, boolean breakable){
		this.type = type;
		this.breakable = breakable;
	}
	
	private Material(MaterialType type, boolean breakable, boolean collisions){
		this.type = type;
		this.breakable = breakable;
		this.enablecollisions = collisions;
	}
	
	private Material(MaterialType type, Color foilageColor){
		this.type = type;
		this.foilageColor = foilageColor;
	}
	
	private Material(Color foilageColor){
		this.foilageColor = foilageColor;
	}
	
	public Color foilageColor(){ // the color that plants have on this tile
		return foilageColor;
	}
	
	public boolean breakable(){ // if the block is breakable (controls whether or not it is searched)
		return breakable;
	}
	
	public boolean collisionsEnabled(){ //if collisions are enabled
		return enablecollisions;
	}
	
	public MaterialType getType(){ //the type
		return type;
	}
	
	public void harvestEvent(Tile tile){ //called when the block is harvested/destroyed
		if(getType().tile()){
			//tile.tile = Material.air;
		}else{
			tile.block = Material.air;
		}
	}
	
	public void changeEvent(Tile tile){ //called when the block is changed/updated
		tile.checkData(this, getDataClass());
	}
	
	public TileData getDefaultData(){
		return null;
	}
	
	public Class<? extends TileData> getDataClass(){
		return TileData.class;
	}
	
	public boolean growable(){
		return false;
	}
	
	public Material growMaterial(){
		return null;
	}
	
	public void growEvent(Tile tile){
		tile.block = growMaterial();
	}
	
	public ItemStack getGrowItem(){
		return null;
	}
	
	protected void addDrop(Item item, int amount){
		if(drops == null) drops = new Array<ItemStack>();
		drops.add(new ItemStack(item, amount));
	}
	
	public Array<ItemStack> getDrops(){
		return drops;
	}
	
	public boolean dropsItem(Item item){
		for(ItemStack stack : drops){
			if(stack.item == item) return true;
		}
		return false;
	}
	
	public boolean reserved(){
		return true;
	}
	
	public static Material random(Material... materials){
		return materials[MathUtils.random(materials.length-1)];
	}
	
	public static Material next(Material mat, int max){
		return values()[mat.ordinal() + MathUtils.random(max-1)];
	}
}
